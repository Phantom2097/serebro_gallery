package ru.null_checkers.form_filling_screen.ui.formfilling

import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.widget.doAfterTextChanged
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import ru.null_checkers.form_filling_screen.R
import ru.null_checkers.form_filling_screen.databinding.FragmentFormFillingBinding

/**
 * @author Phantom2097
 */
class FormFilling : Fragment(R.layout.fragment_form_filling) {

    private var _binding: FragmentFormFillingBinding? = null
    val binding get() = _binding!!

    private val viewModel by activityViewModels<FormFillingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormFillingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeFields()
        redefineOnBackPressed()
        initButtons()

        subscribeFormFillingViewModel()

        keyBoardListener()
        clearFieldsFocus()

        val permissions = GetPermissions()()

        ActivityCompat.requestPermissions(
            requireActivity(),
            permissions,
            0
        )
    }

    private fun redefineOnBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    /**
     * Обработка нажатия на экран, для снятия фокуса с полей ввода
     */
    private fun clearFieldsFocus() {
        val button = binding.formFilling
        button.setOnClickListener {
            binding.apply {
                userNameField.clearFocus()
                userTelegramField.clearFocus()
                hideKeyboard()
            }
        }
    }


    /**
     * Функция обеспечивает наблюдение за изменениями данных во View Model
     */
    private fun subscribeFormFillingViewModel() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    viewModel.userFieldsState.collect { (name, tgAcc, file) ->
                        file?.let { (uri, name) ->
                            binding.apply {
                                textForAddImage.text = name

                                Glide.with(requireContext())
                                    .load(uri)
                                    .override(300, 300)
                                    .centerCrop()
                                    .into(buttonForAddImage)
                            }
                        }
                        if (name.isBlank() || tgAcc.isBlank() || file == null) {
                            binding.buttonApplyForAnExhibition.apply {
                                alpha = 0.3f
                                isClickable = false
                            }
                        } else {
                            binding.buttonApplyForAnExhibition.apply {
                                alpha = 1.0f
                                isClickable = true
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Функция отслеживает нажатия на клавиатуре кнопок вперёд и подтвердить
     */
    private fun keyBoardListener() {
        binding.userNameField.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.KEYCODE_ENTER) {
                binding.userTelegramField.requestFocus()
                true
            } else {
                false
            }
        }


        binding.userTelegramField.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER || event.action == KeyEvent.ACTION_DOWN))
            ) {
                binding.userTelegramField.clearFocus()
                hideKeyboard()
                true
            } else {
                false
            }
        }
    }

    /**
     * Сокрытие клавиатуры
     */
    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(InputMethodManager::class.java)
        imm.hideSoftInputFromWindow(binding.userTelegramField.windowToken, 0)
    }

    /**
     * Функция отслеживает обновления текста в полях ввода
     */
    private fun observeFields() {
        binding.apply {
            userNameField.doAfterTextChanged { text ->
                viewModel.updateNameField(text?.trim().toString())
            }

            userTelegramField.doAfterTextChanged { text ->
                viewModel.updateTelegramField(text?.trim().toString())
            }
        }
    }

    /**
     * Инициализация кнопок данного фрагмента
     */
    private fun initButtons() = with(binding) {
        val addButton = buttonForAddImage
        val applyButton = buttonApplyForAnExhibition
        val addCardImage = addImageCard

        addImageFromLocal(addButton)
        addApplyButton(applyButton)
        addImageFromLocal(addCardImage)
    }

    private var isProcessing = false

    /**
     *  Функция добавляет действие выбора изображения из локального хранилища
     */
    private fun addImageFromLocal(view: View) {
        view.setOnClickListener {
            if (!isProcessing) {
//            showImagePickerBottomSheet()
                isProcessing = true
                openGallery()
            }
        }
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        isProcessing = false
        uri?.let { imageUri ->
            val fileName = DocumentFile.fromSingleUri(requireContext(), imageUri)?.name
                ?: uri.lastPathSegment.toString()
            viewModel.onItemClick(MediaFile(uri = imageUri, name = fileName))
        }
    }

//    private fun getFileNameFromUri(uri: Uri): String? {
//        return when (uri.scheme) {
//            "content" -> getContentFileName(uri)
//            "file" -> uri.lastPathSegment
//            else -> null
//        }
//    }
//
//    private fun getContentFileName(uri: Uri): String? {
//        return context?.contentResolver?.query(uri, null, null, null, null)?.use { cursor ->
//            cursor.moveToFirst()
//            val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//            if (displayNameIndex != -1) {
//                cursor.getString(displayNameIndex)
//            } else {
//                null
//            }
//        }
//    }

    private fun openGallery() {
        galleryLauncher.launch(GALLERY_LAUNCHER_FILTER)
    }

    /**
     * Функция добавляет действие отправки данных заполненных в форме
     */
    private fun addApplyButton(view: View) {
        view.setOnClickListener {
            Toast.makeText(requireContext(), "Нужно бы уже это сделать", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Навигация к диалогу
     */
//    private fun showImagePickerBottomSheet() {
//        viewModel.getMediaFiles(requireContext())
//
//        findNavController().navigate(R.id.action_formFilling_to_imagePickerBottomSheet)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private companion object {
        private const val GALLERY_LAUNCHER_FILTER = "image/*"
    }
}
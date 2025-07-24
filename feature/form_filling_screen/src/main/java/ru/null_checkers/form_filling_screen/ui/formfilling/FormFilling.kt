package ru.null_checkers.form_filling_screen.ui.formfilling

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import ru.null_checkers.common.autocomplete.ProfileDataViewModel
import ru.null_checkers.common.autocomplete.ProfileViewModelFactory
import ru.null_checkers.common.models.MediaFile
import ru.null_checkers.form_filling_screen.R
import ru.null_checkers.form_filling_screen.databinding.FragmentFormFillingBinding
import ru.null_checkers.form_filling_screen.domain.factory.DialogFactory
import ru.null_checkers.form_filling_screen.ui.Animation.imagePickSimpleAnimation
import ru.null_checkers.ui.toolbar.ToolbarController

class FormFilling : Fragment() {

    private var _binding: FragmentFormFillingBinding? = null
    val binding get() = _binding!!

    private val viewModel by activityViewModels<FormFillingViewModel>()
    private val dataViewModel: ProfileDataViewModel by viewModels {
        ProfileViewModelFactory(
            requireContext().getSharedPreferences("ProfilePrefs", MODE_PRIVATE)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFormFillingBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle()

        observeFields()
        redefineOnBackPressed()
        initButtons()

        subscribeFormFillingViewModel()

        keyBoardListener()
        clearFieldsFocus()

        view.post {
            checkShowFormAboutDialog()
        }

//        val permissions = GetPermissions()()

        //TODO: Из-за этого происходит мигание
//        ActivityCompat.requestPermissions(
//            requireActivity(),
//            permissions,
//            0
//        )
//        Log.d("FORM_FILLING_FRAGMENT", "ON VIEW CREATED")
        viewLifecycleOwner.lifecycleScope.launch {
            dataViewModel.profileData.collect { profile ->
                profile?.let {
                    binding.userNameField.setText(it.name + " " + it.surname)
                    binding.userTelegramField.setText(it.telegram)
                }
            }
        }
    }

    private fun checkShowFormAboutDialog() {
        val preferences =
            requireContext().getSharedPreferences(DIALOG_ABOUT_FORM, MODE_PRIVATE)
        val dialogNeedShowing = preferences.getBoolean(DIALOG_ABOUT_FORM, true)

        if (dialogNeedShowing) {
            showDialogAboutForm(preferences)
        }
    }

    private fun showDialogAboutForm(preferences: SharedPreferences) {
        DialogFactory().createAboutFormDialog(requireContext(), preferences).show()
    }

    private fun setTitle() {
        (requireActivity() as? ToolbarController)?.setTitle(
            getString(ru.null_checkers.ui.R.string.formFillingFragmentTitle)
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
                                    .override(SELECTED_IMAGE_WIDTH, SELECTED_IMAGE_HEIGHT)
                                    .centerCrop()
                                    .into(buttonForAddImage)

                                addPickImage(buttonForAddImage)
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
        val onlineFormButton = goToOnlineForm

        addImageFromLocal(addButton)
        addImageFromLocal(addCardImage)
        addApplyButton(applyButton)
        addInternetFormButton(onlineFormButton)
    }

    private fun addPickImage(view: View) {
        view.imagePickSimpleAnimation()
    }

    private fun addInternetFormButton(view: View) {
        view.setOnClickListener {
            openUrlWithDialog()
        }
    }

    // Наверное это надо разнести на два useCase
    private fun openUrlWithDialog() = with(viewModel) {
        val formUrl = getUrl()

        showDialog(requireContext(), formUrl)
    }

    private fun sendEmail(
        name: String,
        tgAccount: String,
        file: MediaFile?,
    ) {
        val text = "Участник: $name\nКонтакты: $tgAccount"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            val email = "exampleEmail@yandex.ru"
            val subject = "Форма регистрации участника"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
            file?.let {
                putExtra(Intent.EXTRA_STREAM, it.uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }

        val chooser = Intent.createChooser(intent, "Отправить письмо через...")

        try {
            requireContext().startActivity(chooser)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "Не найдено почтовое приложение$e", Toast.LENGTH_SHORT)
                .show()
        }
    }

//    private var isProcessing = false

    /**
     *  Функция добавляет действие выбора изображения из локального хранилища
     */
    private fun addImageFromLocal(view: View) {
        view.setOnClickListener {
            openGallery()
        }
    }

    /**
     * Открытие диалога с галереей
     */
    private fun openGallery() {
        Log.d("Navigation to dialog", "переход на диалоговое окно")
        viewModel.updateDialogState(true)
        findNavController().navigate(R.id.action_formFilling_to_imagePickFragment)
    }

    /**
     * Функция добавляет действие отправки данных заполненных в форме
     */
    private fun addApplyButton(view: View) {
        view.setOnClickListener {
            val (name, tgAccount, file) = viewModel.getMedia()
            sendEmail(name, tgAccount, file)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val DIALOG_ABOUT_FORM = "Dialog_about_form"

        private const val SELECTED_IMAGE_HEIGHT = 300
        private const val SELECTED_IMAGE_WIDTH = 300
    }
}
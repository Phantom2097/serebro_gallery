package ru.null_checkers.form_filling_screen.ui.image_pick

import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import ru.null_checkers.common.entity.SharedViewModel
import ru.null_checkers.common.models.MediaFile
import ru.null_checkers.common.shared_view_model.PhotoSharedViewModel
import ru.null_checkers.form_filling_screen.R
import ru.null_checkers.form_filling_screen.databinding.FragmentImagePickBinding
import ru.null_checkers.form_filling_screen.ui.formfilling.FormFillingViewModel
import ru.null_checkers.form_filling_screen.ui.image_pick.adapter.ImagePickAdapter
import ru.null_checkers.form_filling_screen.ui.image_pick.adapter.decoration.SpacesItemDecoration

class ImagePickFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentImagePickBinding? = null
    val binding get() = _binding!!

    private lateinit var photoViewModel: PhotoSharedViewModel
    private val formFillingViewModel by activityViewModels<FormFillingViewModel>()

    val pickImageAdapter by lazy { ImagePickAdapter(formFillingViewModel) }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { imageUri ->
            val fileName = DocumentFile.fromSingleUri(requireContext(), imageUri)?.name
                ?: uri.lastPathSegment.toString()

            Log.d("Mapper to MediaFile", "$imageUri")
            formFillingViewModel.onItemClick(MediaFile(uri = imageUri, name = fileName))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentImagePickBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Переопределение метода получения темы, для использования кастомной темы
     */
    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        redefineBackButton()

        getSharedViewModel()
        subscribeViewModel()

        initRecycler()
        initButtons()
    }

    private fun initButtons() {
        val phoneGalleryButton = binding.openPhoneGallery

        initPhoneGalleryButton(phoneGalleryButton)
    }

    fun initPhoneGalleryButton(view: View) {
        view.setOnClickListener {
            galleryLauncher.launch(GALLERY_LAUNCHER_FILTER)
        }
    }

    /**
     * Инициализация общей вью модели для получения добавленных фотографий
     */
    private fun getSharedViewModel() {
        photoViewModel = (requireActivity() as SharedViewModel).getSharedPhotoViewModel().apply {
            getGalleryPhotos()
        }
    }

    /**
     * Инициализация списка добавленных фотографий
     */
    private fun initRecycler() {
        val recyclerView = binding.imagePickList

        recyclerView.apply {
            layoutManager = GridLayoutManager(context, SPAN_COUNT)
            adapter = pickImageAdapter

            addItemDecoration(SpacesItemDecoration(4))
        }
    }

    /**
     * Подписка на получение списка фотографий из общей вью модели
     */
    private fun subscribeViewModel() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    photoViewModel.state.collect { list ->
                        pickImageAdapter.submitList(list)
                    }
                }
                launch {
                    formFillingViewModel.dialogState.collect {
                        if (!it) {
                            dismiss()
                        }
                    }
                }
            }
        }
    }

    /**
     * Переопределение кнопки назад для корректного перехода
     */
    private fun redefineBackButton() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                dismiss()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    /**
     * Переопределение метода скрытия диалогового окна для корректного перехода
     */
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        formFillingViewModel.updateDialogState(false)
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private companion object {
        private const val SPAN_COUNT = 3

        private const val GALLERY_LAUNCHER_FILTER = "image/*"
    }
}
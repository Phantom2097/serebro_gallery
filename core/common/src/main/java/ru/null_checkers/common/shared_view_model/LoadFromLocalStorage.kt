package ru.null_checkers.common.shared_view_model

import android.net.Uri
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import ru.null_checkers.common.models.Photo

class LoadFromLocalStorage(
    private val fragment: Fragment,
    private val viewModel: PhotoSharedViewModel
) {
    private var isProcessing = false

    private val galleryLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        isProcessing = false
        uri?.let { imageUri ->
            val photo = Photo(
                id = System.currentTimeMillis(),
                imagePath = imageUri.toString(),
                isFavorite = false
            )
            viewModel.currPhoto.value = photo
        }
    }
    fun addImageFromLocal(view: View) {
        view.setOnClickListener {
            if (!isProcessing) {
                isProcessing = true
                openGallery()
            }
        }
    }
    private fun openGallery() {
        galleryLauncher.launch(GALLERY_LAUNCHER_FILTER)
    }

    private companion object {
        private const val GALLERY_LAUNCHER_FILTER = "image/*"
    }
}
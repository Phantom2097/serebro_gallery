package com.example.serebro_gallery.common.utils

import android.net.Uri
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.serebro_gallery.domain.models.Photo
import com.example.serebro_gallery.presentation.viewmodel.PhotoViewModel

class LoadFromLocalStorage(
    private val fragment: Fragment,
    private val viewModel: PhotoViewModel
) {
    private var isProcessing = false
    private val GALLERY_LAUNCHER_FILTER = "image/*"

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
}
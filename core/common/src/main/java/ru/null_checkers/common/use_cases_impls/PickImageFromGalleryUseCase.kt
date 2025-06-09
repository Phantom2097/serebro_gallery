package ru.null_checkers.common.use_cases_impls

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.FragmentActivity
import ru.null_checkers.common.models.MediaFile
import ru.null_checkers.common.use_cases.PickImageFromGallery

// Не используется на данный момент, нужно поменять
class PickImageFromGalleryUseCase : PickImageFromGallery {
    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    override fun invoke(
        activity: FragmentActivity,
        onImageSelected: (MediaFile) -> Unit
    ) {
        galleryLauncher = activity.registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let { imageUri ->
                val fileName = DocumentFile.fromSingleUri(activity, imageUri)?.name
                    ?: uri.lastPathSegment.toString()
                onImageSelected(MediaFile(uri = imageUri, name = fileName))
            }
        }

        // this.galleryLauncher = galleryLauncher //.launch(GALLERY_LAUNCHER_FILTER)
    }

    fun openGallery() {
        galleryLauncher.launch(GALLERY_LAUNCHER_FILTER)
    }

    private companion object {
        private const val GALLERY_LAUNCHER_FILTER = "image/*"
    }
}
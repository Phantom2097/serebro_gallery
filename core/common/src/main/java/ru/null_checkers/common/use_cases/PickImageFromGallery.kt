package ru.null_checkers.common.use_cases

import androidx.fragment.app.FragmentActivity
import ru.null_checkers.common.models.MediaFile

interface PickImageFromGallery {
    operator fun invoke(
        activity: FragmentActivity,
        onImageSelected: (MediaFile) -> Unit
    )
}
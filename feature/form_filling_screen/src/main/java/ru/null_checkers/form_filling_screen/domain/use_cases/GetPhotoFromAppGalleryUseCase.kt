package ru.null_checkers.form_filling_screen.domain.use_cases

import ru.null_checkers.common.models.MediaFile

interface GetPhotoFromAppGalleryUseCase {
    suspend operator fun invoke(): MediaFile
}
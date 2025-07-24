package ru.null_checkers.form_filling_screen.domain.mappers

import android.util.Log
import androidx.core.net.toUri
import ru.null_checkers.common.models.MediaFile
import ru.null_checkers.common.models.Photo

object Mappers {
    fun Photo.toMediaFile(): MediaFile {
        Log.d("Mapper to MediaFile", "${imagePath.toUri()}")

        val fileName = imagePath.toUri().lastPathSegment.toString()
        return MediaFile(
            imagePath.toUri(),
            fileName
        )
    }
}
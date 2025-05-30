package ru.null_checkers.form_filling_screen.domain.use_cases_impls

import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.provider.BaseColumns._ID
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
import android.provider.MediaStore.MediaColumns.DISPLAY_NAME
import ru.null_checkers.form_filling_screen.domain.use_cases.ShowImagesFromLocalStorage
import ru.null_checkers.form_filling_screen.ui.formfilling.MediaFile

/**
 * @author Phantom2097
 */
// Вообще наверное должно быть в дата модуле
@Deprecated("Судя по всему это было не нужно")
class ShowImagesFromLocalStorageUseCase : ShowImagesFromLocalStorage {
    override suspend fun invoke(context: Context): List<MediaFile> {
        val mediaFiles = mutableListOf<MediaFile>()

        val queryUri = if (Build.VERSION.SDK_INT >= 29) {
            EXTERNAL_CONTENT_URI
        } else {
            MediaStore.Images.Media.getContentUri("external")
        }

        val projection = arrayOf(_ID, DISPLAY_NAME)

        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        context.contentResolver.query(
            queryUri,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(_ID)
            val nameColumn = cursor.getColumnIndexOrThrow(DISPLAY_NAME)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)

                name?.let {
                    val contentUri = ContentUris.withAppendedId(queryUri, id)
                    mediaFiles.add(MediaFile(uri = contentUri, name = name)
                    )
                }
            }
        }
        return mediaFiles
    }
}
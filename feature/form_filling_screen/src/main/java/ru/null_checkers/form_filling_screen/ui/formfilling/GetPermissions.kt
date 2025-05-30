package ru.null_checkers.form_filling_screen.ui.formfilling

import android.Manifest
import android.os.Build

/**
 * @author Phantom2097
 */
class GetPermissions {
    operator fun invoke() : Array<String> = if (Build.VERSION.SDK_INT >= 33) {
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
        )
    } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
}
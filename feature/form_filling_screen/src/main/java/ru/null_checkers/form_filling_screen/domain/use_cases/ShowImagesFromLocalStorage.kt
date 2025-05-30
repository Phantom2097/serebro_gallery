package ru.null_checkers.form_filling_screen.domain.use_cases

import android.content.Context
import ru.null_checkers.form_filling_screen.ui.formfilling.MediaFile

/**
 * Функция получения изображений из локального хранилища устройства
 *
 * @see "https://www.youtube.com/watch?v=ji6Z32oPUpQ"
 *
 * @author Phantom2097
 */
interface ShowImagesFromLocalStorage {
    suspend operator fun invoke(context: Context): List<MediaFile>
}
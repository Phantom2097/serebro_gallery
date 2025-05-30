package ru.null_checkers.form_filling_screen.ui.select_image_from_storage.recycler

import ru.null_checkers.form_filling_screen.ui.formfilling.MediaFile

fun interface OnItemClick {
    fun onItemClick(file: MediaFile)
}
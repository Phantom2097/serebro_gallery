package ru.null_checkers.form_filling_screen.ui.formfilling

import ru.null_checkers.common.models.MediaFile

fun interface OnItemClick {
    fun onItemClick(file: MediaFile)
}
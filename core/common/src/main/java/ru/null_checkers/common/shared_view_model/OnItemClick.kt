package ru.null_checkers.common.shared_view_model

import ru.null_checkers.common.models.MediaFile

fun interface OnItemClick {
    fun onItemClick(file: MediaFile)
}
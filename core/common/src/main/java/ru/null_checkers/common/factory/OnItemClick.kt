package ru.null_checkers.common.factory

import ru.null_checkers.common.models.MediaFile

fun interface OnItemClick {
    fun onItemClick(file: MediaFile)
}
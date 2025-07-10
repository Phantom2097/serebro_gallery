package ru.null_checkers.common.models

data class Photo(
    val id: Long,
    val imagePath: String,
    val isFavorite: Boolean
)
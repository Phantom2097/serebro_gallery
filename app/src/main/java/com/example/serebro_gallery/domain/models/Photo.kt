package com.example.serebro_gallery.domain.models

data class Photo(
    val id: Long,
    val imagePath: String,
    val isFavorite: Boolean
)
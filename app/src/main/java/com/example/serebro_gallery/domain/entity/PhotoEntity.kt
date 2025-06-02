package com.example.serebro_gallery.domain.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val imagePath: String,

    val isFavorite: Boolean,
)
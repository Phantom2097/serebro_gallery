package ru.null_checkers.data.local.entity
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "photos",
    indices = [Index(value = ["imagePath"], unique = true)]
)
data class PhotoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val imagePath: String,

    val isFavorite: Boolean,

    val compId: String
)
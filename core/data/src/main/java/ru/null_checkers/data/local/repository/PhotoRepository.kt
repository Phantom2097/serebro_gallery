package ru.null_checkers.data.local.repository

import kotlinx.coroutines.flow.Flow
import ru.null_checkers.common.models.Photo
import ru.null_checkers.data.local.entity.PhotoEntity

interface PhotoRepository {
    fun getGalleryPhotosFlow(): Flow<List<Photo>>
    fun getFavoritesFlow(): Flow<List<Photo>>

    suspend fun addPhoto(photo: PhotoEntity)
    suspend fun toggleFavorite(photo: PhotoEntity)
}
package ru.null_checkers.data.local.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.null_checkers.data.local.dao.PhotoDao
import ru.null_checkers.data.local.entity.PhotoEntity
import ru.null_checkers.common.models.Photo

class PhotoRepository(private val photoDao: PhotoDao) {
    fun PhotoEntity.toPhoto(): Photo {
        return Photo(
            id = this.id,
            imagePath = this.imagePath,
            isFavorite = this.isFavorite
        )
    }

    fun getGalleryPhotosFlow(): Flow<List<Photo>> =
        photoDao.getGalleryPhotos().map { entities ->
            entities.map { it.toPhoto() }
        }

    fun getFavoritesFlow(): Flow<List<Photo>> =
        photoDao.getFavorites().map { entities ->
            entities.map { it.toPhoto() }
        }


    suspend fun addPhoto(photo: PhotoEntity) = photoDao.insert(photo)

    suspend fun toggleFavorite(photo: PhotoEntity) {
        val newFavoriteStatus = !photo.isFavorite
        photoDao.setFavorite(photo.id, newFavoriteStatus)
        photoDao.update(photo.copy(isFavorite = newFavoriteStatus))
    }
}
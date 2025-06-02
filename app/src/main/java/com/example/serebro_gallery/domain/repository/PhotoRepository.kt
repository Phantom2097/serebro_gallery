package com.example.serebro_gallery.domain.repository

import com.example.serebro_gallery.domain.PhotoDao
import com.example.serebro_gallery.domain.entity.PhotoEntity
import com.example.serebro_gallery.domain.models.Photo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PhotoRepository(private val photoDao: PhotoDao) {
    private fun PhotoEntity.toPhoto(): Photo {
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
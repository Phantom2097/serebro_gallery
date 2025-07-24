package ru.null_checkers.data.local.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.null_checkers.data.local.dao.PhotoDao
import ru.null_checkers.data.local.entity.PhotoEntity
import ru.null_checkers.common.models.Photo

class PhotoRepositoryImpl(private val photoDao: PhotoDao) : PhotoRepository {
    private fun PhotoEntity.toPhoto(): Photo {
        return Photo(
            id = this.id,
            imagePath = this.imagePath,
            isFavorite = this.isFavorite,
            compId = this.compId
        )
    }

    override fun getGalleryPhotosFlow(): Flow<List<Photo>> =
        photoDao.getGalleryPhotos().map { entities ->
            entities.map { it.toPhoto() }
        }

    override fun getFavoritesFlow(): Flow<List<Photo>> =
        photoDao.getFavorites().map { entities ->
            entities.map { it.toPhoto() }
        }


    override suspend fun addPhoto(photo: PhotoEntity) = photoDao.insert(photo)

    override suspend fun toggleFavorite(photo: PhotoEntity) {
        val newFavoriteStatus = !photo.isFavorite
        photoDao.setFavorite(photo.id, newFavoriteStatus)
        photoDao.update(photo.copy(isFavorite = newFavoriteStatus))
    }


    suspend fun deleteFavorite(photo: PhotoEntity) {
        photoDao.deleteFavorite(photo.compId)
    }
}
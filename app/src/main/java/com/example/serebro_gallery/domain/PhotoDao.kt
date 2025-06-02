package com.example.serebro_gallery.domain

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.serebro_gallery.domain.entity.PhotoEntity
import com.example.serebro_gallery.domain.models.Photo
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photos WHERE isFavorite = 1")
     fun getFavorites(): Flow<List<PhotoEntity>>

    @Query("SELECT * FROM photos WHERE isFavorite = 0")
     fun getGalleryPhotos(): Flow<List<PhotoEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: PhotoEntity)

    @Update
    suspend fun update(photo: PhotoEntity)

    @Query("UPDATE photos SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean)
}
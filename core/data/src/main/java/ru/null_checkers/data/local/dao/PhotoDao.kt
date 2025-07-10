package ru.null_checkers.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.null_checkers.data.local.entity.PhotoEntity

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photos WHERE isFavorite = 1")
    fun getFavorites(): Flow<List<PhotoEntity>>

    @Query("SELECT * FROM photos WHERE isFavorite = 0")
    fun getGalleryPhotos(): Flow<List<PhotoEntity>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(photo: PhotoEntity)

    @Update
    suspend fun update(photo: PhotoEntity)

    @Query("UPDATE photos SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean)
}
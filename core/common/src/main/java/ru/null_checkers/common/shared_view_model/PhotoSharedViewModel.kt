package ru.null_checkers.common.shared_view_model

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.StateFlow
import ru.null_checkers.common.models.Photo

interface PhotoSharedViewModel {
    val state: StateFlow<List<Photo>>

    val favorite: StateFlow<List<Photo>>

    val currPhoto: MutableLiveData<Photo>

    fun addItem(item: Photo)
    fun getGalleryPhotos()
    fun getFavorite()
    fun deleteFavorite(item: Photo)
}
package com.example.serebro_gallery.presentation.shared_viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.null_checkers.common.models.Photo
import ru.null_checkers.common.shared_view_model.PhotoSharedViewModel
import ru.null_checkers.data.local.entity.PhotoEntity
import ru.null_checkers.data.local.repository.PhotoRepository

class PhotoViewModel(private val repository: PhotoRepository) : ViewModel(), PhotoSharedViewModel {

    private val _state = MutableStateFlow<List<Photo>>(listOf())
    override val state: StateFlow<List<Photo>> = _state.asStateFlow()

    private val _favorite = MutableStateFlow<List<Photo>>(listOf())
    override val favorite: StateFlow<List<Photo>> = _favorite.asStateFlow()

    override var currPhoto = MutableLiveData<Photo>()

    //    fun toggleFavorite(photo: Photo) {
//        viewModelScope.launch {
//            repository.toggleFavorite(photo.toEntity())
//            getFavorite()
//        }
//    }
    override fun addItem(item: Photo) {
        viewModelScope.launch {
            repository.addPhoto(item.toEntity())
        }
    }

    override fun getGalleryPhotos() {
        viewModelScope.launch {
            repository.getGalleryPhotosFlow()
                .collect { photos ->
                    _state.value = photos
                }
        }
    }

    override fun getFavorite() {
        viewModelScope.launch {
            repository.getFavoritesFlow()
                .collect { photos ->
                    _favorite.value = photos
                }
        }
    }

    private fun Photo.toEntity(): PhotoEntity = PhotoEntity(
        id = id,
        imagePath = imagePath,
        isFavorite = isFavorite,
        compId = compId
    )
}
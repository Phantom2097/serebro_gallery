package ru.null_checkers.common.setup_view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serebro_gallery.domain.entity.PhotoEntity
import com.example.serebro_gallery.domain.models.Photo
import com.example.serebro_gallery.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.null_checkers.common.entity.PhotoEntity
import ru.null_checkers.common.models.Photo
import ru.null_checkers.common.repository.PhotoRepository

class PhotoViewModel(private val repository: PhotoRepository) : ViewModel() {

    private val _state = MutableStateFlow<List<Photo>>(listOf())
    val state: StateFlow<List<Photo>> = _state.asStateFlow()

    private val _favorite = MutableStateFlow<List<Photo>>(listOf())
    val favorite: StateFlow<List<Photo>> = _favorite.asStateFlow()

    var currPhoto = MutableLiveData<Photo>()

//    fun toggleFavorite(photo: Photo) {
//        viewModelScope.launch {
//            repository.toggleFavorite(photo.toEntity())
//            getFavorite()
//        }
//    }
    fun addItem(item: Photo) {
        viewModelScope.launch {
            repository.addPhoto(item.toEntity())
        }
    }

    fun getGalleryPhotos() {
        viewModelScope.launch {
            repository.getGalleryPhotosFlow()
                .collect { photos ->
                    _state.value = photos
                }
        }
    }
    fun getFavorite(){
        viewModelScope.launch {
            repository.getFavoritesFlow()
                .collect { photos ->
                    _favorite.value = photos
                }
        }
    }

    fun Photo.toEntity(): PhotoEntity = PhotoEntity(
        id = id,
        imagePath = imagePath,
        isFavorite = isFavorite
    )
}
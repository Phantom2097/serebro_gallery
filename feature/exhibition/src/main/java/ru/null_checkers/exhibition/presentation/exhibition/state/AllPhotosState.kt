package ru.null_checkers.exhibition.presentation.exhibition_list.state

import ru.null_checkers.exhibition.models.PhotoItem
import ru.null_checkers.exhibition.models.PrizePhoto

sealed class AllPhotosState {
    object Loading : AllPhotosState()
    data class Success(val photos: List<PhotoItem>) : AllPhotosState()
    data class Error(val message: String) : AllPhotosState()
}
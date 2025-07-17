package ru.null_checkers.exhibition.presentation.exhibition_list.state

import ru.null_checkers.exhibition.models.PhotoItem

sealed class PrizePhotoState {
    object Loading : PrizePhotoState()
    data class Success(val exhibitions: PhotoItem) : PrizePhotoState()
    data class Error(val message: String) : PrizePhotoState()
}
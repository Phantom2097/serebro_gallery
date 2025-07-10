package ru.null_checkers.exhibition.presentation.exhibition_list.state

import ru.null_checkers.exhibition.models.ExhibitionItem

sealed class ExhibitionsState {
    object Loading : ExhibitionsState()
    data class Success(val exhibitions: List<ExhibitionItem>) : ExhibitionsState()
    data class Error(val message: String) : ExhibitionsState()
}
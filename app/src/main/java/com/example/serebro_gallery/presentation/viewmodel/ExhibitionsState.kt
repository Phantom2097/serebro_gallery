package com.example.serebro_gallery.presentation.viewmodel

import androidx.lifecycle.LiveData
import com.example.serebro_gallery.domain.models.ExhibitionItem

sealed class ExhibitionsState {
    object Loading : ExhibitionsState()
    data class Success(val exhibitions: List<ExhibitionItem>) : ExhibitionsState()
    data class Error(val message: String) : ExhibitionsState()
}

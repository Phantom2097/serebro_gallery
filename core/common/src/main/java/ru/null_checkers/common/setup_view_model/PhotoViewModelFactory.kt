package ru.null_checkers.common.setup_view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PhotoViewModelFactory {

    // Фабрика для ViewModel
    @Suppress("UNCHECKED_CAST")
    private val viewModelFactory by lazy {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PhotoViewModel(repository) as T
            }
        }
    }
}
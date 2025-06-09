package com.example.serebro_gallery.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.null_checkers.common.models.MediaFile
import ru.null_checkers.form_filling_screen.domain.models.UserForm
import ru.null_checkers.form_filling_screen.ui.formfilling.OnItemClick

class ProfileViewModel(
) : ViewModel(), OnItemClick {

    private var _userFieldsState = MutableStateFlow<UserForm>(UserForm())
    val userFieldsState = _userFieldsState.asStateFlow()

    override fun onItemClick(file: MediaFile) {
        _userFieldsState.update { it.copy(image = file) }
    }
}
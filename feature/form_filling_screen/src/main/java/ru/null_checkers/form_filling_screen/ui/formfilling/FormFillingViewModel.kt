package ru.null_checkers.form_filling_screen.ui.formfilling

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.null_checkers.common.factory.OnItemClick
import ru.null_checkers.common.models.MediaFile
import ru.null_checkers.common.models.UserForm
import ru.null_checkers.form_filling_screen.domain.use_cases_impls.SendDataToForm

/**
 * @author Phantom2097
 */
class FormFillingViewModel() : ViewModel(), OnItemClick {

    private var _userFieldsState = MutableStateFlow<UserForm>(UserForm())
    val userFieldsState = _userFieldsState.asStateFlow()

    fun updateNameField(newName: String = NAME_FIELD_EMPTY) {
        _userFieldsState.update { it.copy(name = newName) }
    }

    fun updateTelegramField(newTg: String = TELEGRAM_FIELD_EMPTY) {
        _userFieldsState.update { it.copy(tgAccount = newTg) }
    }

    override fun onItemClick(file: MediaFile) {
        _userFieldsState.update { it.copy(image = file) }
    }

    fun goToOnlineForm() : String {
        userFieldsState.value.apply {
            return SendDataToForm.sendDataToForm(name, tgAccount)
        }
    }

    fun getMedia() = userFieldsState.value

    private companion object {
        private const val NAME_FIELD_EMPTY = ""
        private const val TELEGRAM_FIELD_EMPTY = ""
    }
}
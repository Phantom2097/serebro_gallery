package ru.null_checkers.form_filling_screen.ui.formfilling

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.null_checkers.common.models.MediaFile
import ru.null_checkers.common.models.UserForm
import ru.null_checkers.common.shared_view_model.OnItemClick
import ru.null_checkers.form_filling_screen.domain.factory.UrlForOnlineFormFactory
import ru.null_checkers.form_filling_screen.domain.use_cases.ShowOpenUrlDialog
import ru.null_checkers.form_filling_screen.domain.use_cases_impls.ShowOpenUrlDialogUseCase

class FormFillingViewModel(
    private val showOpenUrlDialog: ShowOpenUrlDialog = ShowOpenUrlDialogUseCase(),
//    private val pickImageFromGallery: PickImageFromGallery = PickImageFromGalleryUseCase(),
) : ViewModel(), OnItemClick {

    private val _userFieldsState = MutableStateFlow<UserForm>(UserForm())
    val userFieldsState = _userFieldsState.asStateFlow()

    private val _dialogState = MutableStateFlow<Boolean>(false)
    val dialogState = _dialogState.asStateFlow()

    /**
     * Обновление поля имени
     *
     * @param newName Имя пользователя, по умолчанию заполняется пустой строкой
     */
    fun updateNameField(newName: String = NAME_FIELD_EMPTY) {
        _userFieldsState.update { it.copy(name = newName) }
    }

    /**
     * Обновление поля никнейма телеграмм
     *
     * @param newTg Никнейм пользователя, по умолчанию заполняется пустой строкой
     */
    fun updateTelegramField(newTg: String = TELEGRAM_FIELD_EMPTY) {
        _userFieldsState.update { it.copy(tgAccount = newTg) }
    }

    /**
     * Обновление состояния диалога
     *
     * @param state Новое состояние
     */
    fun updateDialogState(state: Boolean) {
        _dialogState.update { state }
    }

    override fun onItemClick(file: MediaFile) {
        _userFieldsState.update { it.copy(image = file) }

        updateDialogState(false)
    }

    fun getUrl(): String {
        userFieldsState.value.apply {
            return UrlForOnlineFormFactory.sendDataToForm(name, tgAccount)
        }
    }

    fun showDialog(context: Context, url: String) {
        showOpenUrlDialog(context, url)
    }

    // Пока не используется
//    fun openGallery(activity: FragmentActivity) {
//        pickImageFromGallery(activity) { mediaFile ->
//            onItemClick(mediaFile)
//        }
//    }

    fun getMedia() = userFieldsState.value

    private companion object {
        private const val NAME_FIELD_EMPTY = ""
        private const val TELEGRAM_FIELD_EMPTY = ""
    }
}
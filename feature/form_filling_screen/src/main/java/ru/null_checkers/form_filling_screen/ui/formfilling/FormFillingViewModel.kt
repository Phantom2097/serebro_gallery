package ru.null_checkers.form_filling_screen.ui.formfilling

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.null_checkers.common.models.MediaFile
import ru.null_checkers.common.models.UserForm
import ru.null_checkers.common.shared_view_model.OnItemClick
import ru.null_checkers.common.use_cases.PickImageFromGallery
import ru.null_checkers.common.use_cases_impls.PickImageFromGalleryUseCase
import ru.null_checkers.form_filling_screen.domain.factory.UrlForOnlineFormFactory
import ru.null_checkers.form_filling_screen.domain.use_cases.ShowOpenUrlDialog
import ru.null_checkers.form_filling_screen.domain.use_cases_impls.ShowOpenUrlDialogUseCase

class FormFillingViewModel(
    private val showOpenUrlDialog: ShowOpenUrlDialog = ShowOpenUrlDialogUseCase(),
    private val pickImageFromGallery: PickImageFromGallery = PickImageFromGalleryUseCase(),
) : ViewModel(), OnItemClick {

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

    fun getUrl(): String {
        userFieldsState.value.apply {
            return UrlForOnlineFormFactory.sendDataToForm(name, tgAccount)
        }
    }

    fun showDialog(context: Context, url: String) {
        showOpenUrlDialog(context, url)
    }

    // Пока не используется
    fun openGallery(activity: FragmentActivity) {
        pickImageFromGallery(activity) { mediaFile ->
            onItemClick(mediaFile)
        }
    }

    fun getMedia() = userFieldsState.value

    private companion object {
        private const val NAME_FIELD_EMPTY = ""
        private const val TELEGRAM_FIELD_EMPTY = ""
    }
}
package ru.null_checkers.form_filling_screen.ui.formfilling

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.null_checkers.form_filling_screen.ui.select_image_from_storage.recycler.OnItemClick

/**
 * @author Phantom2097
 */
class FormFillingViewModel(
//    private val showImagesFromLocalStorage: ShowImagesFromLocalStorage = ShowImagesFromLocalStorageUseCase(),
//    private val pickImageFromLocalStorage: PickImageFromLocalStorage = PickImageFromLocalStorageUseCase()
) : ViewModel(), OnItemClick {

    private var _userFieldsState = MutableStateFlow<UserForm>(UserForm())
    val userFieldsState = _userFieldsState.asStateFlow()

    private var _mediaDataState = MutableStateFlow<List<MediaFile>>(emptyList())
    val mediaDataState = _mediaDataState.asStateFlow()

    fun updateNameField(newName: String = NAME_FIELD_EMPTY) {
        _userFieldsState.update { it.copy(name = newName) }
    }

    fun updateTelegramField(newTg: String = TELEGRAM_FIELD_EMPTY) {
        _userFieldsState.update { it.copy(tgAccount = newTg) }
    }

//    fun getMediaFiles(context: Context) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val list = showImagesFromLocalStorage(context)
//            _mediaDataState.update { list }
//            Log.d("imagePick", "${_mediaDataState.value}")
//        }
//    }

    override fun onItemClick(file: MediaFile) {
        _userFieldsState.update { it.copy(image = file) }
    }

    private companion object {
        private const val NAME_FIELD_EMPTY = ""
        private const val TELEGRAM_FIELD_EMPTY = ""
    }
}
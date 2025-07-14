package ru.null_checkers.common.autocomplete

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileDataViewModel(
    private val preferences: SharedPreferences
) : ViewModel() {

    private val _profileData = MutableStateFlow<ProfileData?>(null)
    val profileData: StateFlow<ProfileData?> = _profileData.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        val name = preferences.getString("NAME", null)
        val surname = preferences.getString("SURNAME", null)
        val telegram = preferences.getString("TG", null)

        if (name != null && surname != null && telegram != null) {
            _profileData.value = ProfileData(name, surname, telegram)
        }
    }

    fun saveProfile(name: String, surname: String, telegram: String) {
        preferences.edit {
            putString("NAME", name)
            putString("SURNAME", surname)
            putString("TG", telegram)
        }
        _profileData.value = ProfileData(name, surname, telegram)
    }
}

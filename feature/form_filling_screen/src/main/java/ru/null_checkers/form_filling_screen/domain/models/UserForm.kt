package ru.null_checkers.form_filling_screen.domain.models

import ru.null_checkers.common.models.MediaFile

data class UserForm(
    val name: String = "",
    val tgAccount: String = "",
    val image: MediaFile? = null
)
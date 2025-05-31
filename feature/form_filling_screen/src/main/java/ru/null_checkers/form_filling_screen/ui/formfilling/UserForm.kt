package ru.null_checkers.form_filling_screen.ui.formfilling

data class UserForm(
    val name: String = "",
    val tgAccount: String = "",
    val image: MediaFile? = null
)
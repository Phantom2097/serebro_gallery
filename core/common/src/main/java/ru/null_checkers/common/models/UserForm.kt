package ru.null_checkers.common.models

data class UserForm(
    val name: String = "",
    val tgAccount: String = "",
    val image: MediaFile? = null
)
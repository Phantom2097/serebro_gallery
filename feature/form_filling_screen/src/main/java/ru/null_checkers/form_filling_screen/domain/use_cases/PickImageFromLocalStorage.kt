package ru.null_checkers.form_filling_screen.domain.use_cases


interface PickImageFromLocalStorage {
    suspend operator fun invoke(): String
}
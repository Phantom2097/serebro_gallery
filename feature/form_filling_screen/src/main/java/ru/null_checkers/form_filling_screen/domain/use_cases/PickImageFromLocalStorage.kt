package ru.null_checkers.form_filling_screen.domain.use_cases

/**
 * @author Phantom2097
 */
interface PickImageFromLocalStorage {
    suspend operator fun invoke() : String
}
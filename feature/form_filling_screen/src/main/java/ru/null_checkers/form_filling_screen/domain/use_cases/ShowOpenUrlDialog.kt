package ru.null_checkers.form_filling_screen.domain.use_cases

import android.content.Context

/**
 * Пользовательская функция для открытия диалогового окна
 *
 * Согласие - переход по ссылке
 * Отмена - скрытие диалога
 */
interface ShowOpenUrlDialog {
    operator fun invoke(context: Context, url: String)
}
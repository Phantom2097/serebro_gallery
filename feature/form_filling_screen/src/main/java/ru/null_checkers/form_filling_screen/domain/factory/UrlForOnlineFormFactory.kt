package ru.null_checkers.form_filling_screen.domain.factory

import android.util.Log

object UrlForOnlineFormFactory {
    private const val BASE_URL = "https://forms.yandex.ru/u/683a236cd046885ec7629e67/"
    private const val NAME_FILED = "user_name_text"
    private const val TELEGRAM_FILED = "user_telegram_text"

    private const val FIELD_ASSIGNMENT = '='

    private const val SEPARATOR_FIRST = '?'
    private const val SEPARATOR_NOT_FIRST = '&'
    private const val SEPARATOR_CHECK = '/'

    fun sendDataToForm(name: String, tgAcc: String): String {
        return buildString {
            append(BASE_URL)
            addParam(name, NAME_FILED)
            addParam(tgAcc, TELEGRAM_FILED)
            Log.d("URL_FORM", this.toString())
        }
    }

    private fun StringBuilder.addParam(param: String, field: String) {
        if (param.isNotBlank()) {
            val separator = if (this.endsWith(SEPARATOR_CHECK)) SEPARATOR_FIRST else SEPARATOR_NOT_FIRST
            append("$separator$field$FIELD_ASSIGNMENT$param")
        }
    }
}
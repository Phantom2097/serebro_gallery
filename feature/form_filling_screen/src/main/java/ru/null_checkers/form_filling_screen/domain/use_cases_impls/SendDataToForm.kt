package ru.null_checkers.form_filling_screen.domain.use_cases_impls

object SendDataToForm {
    private const val BASE_URL = "https://forms.yandex.ru/u/683a236cd046885ec7629e67/"
    private const val NAME_FILED = "user_name_text"
    private const val TELEGRAM_FILED = "user_telegram_text"

    fun sendDataToForm(name: String, tgAcc: String): String {
        return buildString {
            append(BASE_URL)
            addParam(name, NAME_FILED)
            addParam(tgAcc, TELEGRAM_FILED)
        }
    }

    private fun StringBuilder.addParam(param: String, field: String) {
        if (param.isNotBlank()) {
            val separator = if (this.endsWith('/')) "?" else "&"
            append("$separator$field=$param")
        }
    }
}
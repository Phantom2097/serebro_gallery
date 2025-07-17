package ru.null_checkers.exhibition.domain

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

object CheckDeadline {

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkDeadline(description: String?): Boolean {
        val hasDeadline = description?.contains("дедлайн", ignoreCase = true) ?: return false

        if (!hasDeadline) return false

        // Парсим дату дедлайна из описания
        val deadlineDate = parseDeadlineDate(description)

        // Если не удалось распарсить дату - считаем что дедлайн актуален
        deadlineDate ?: return true

        // Сравниваем с текущей датой
        return !deadlineDate.isBefore(LocalDate.now())
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseDeadlineDate(description: String): LocalDate? {
        return try {
            val pattern = """дедлайн\s*—\s*(\d{1,2})\s+([а-я]+)""".toRegex(RegexOption.IGNORE_CASE)

            val match = pattern.find(description) ?: return null

            val day = match.groupValues[1].toInt()
            val monthName = match.groupValues[2]

            // Конвертируем название месяца в номер
            val month = when (monthName.lowercase()) {
                "января" -> 1
                "февраля" -> 2
                "марта"-> 3
                "апреля" -> 4
                "мая"-> 5
                "июня"-> 6
                "июля"-> 7
                "августа" -> 8
                "сентября"-> 9
                "октября" -> 10
                "ноября" -> 11
                "декабря" -> 12
                else -> return null
            }

            // Создаем дату (текущий год + найденные день/месяц)
            LocalDate.of(LocalDate.now().year, month, day)
        } catch (e: Exception) {
            null
        }
    }

}
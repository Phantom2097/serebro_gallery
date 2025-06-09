package ru.null_checkers.form_filling_screen.domain.factory

import android.content.Context
import androidx.appcompat.app.AlertDialog
import ru.null_checkers.common.use_cases.OpenUrl
import ru.null_checkers.common.use_cases_impls.OpenUrlUseCase

object DialogFactory {

    private val openUrl : OpenUrl = OpenUrlUseCase()

    fun createUrlOpenDialog(context: Context, url: String) : AlertDialog.Builder {
        return AlertDialog.Builder(context)
            .setTitle("Подтверждение")
            .setMessage("Вы уверены, что хотите заполнить форму онлайн?\nЗаполненные поля сохранятся, но изображение придётся выбрать заново")
            .setPositiveButton("Oк") { _, _ ->
                openUrl(context, url)
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            .setIcon(ru.null_checkers.ui.R.drawable.logo_white_2)
            .setCancelable(true)
    }
}
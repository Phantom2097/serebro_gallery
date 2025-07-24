package ru.null_checkers.form_filling_screen.domain.factory

import android.content.Context
import android.content.SharedPreferences
import android.widget.CheckBox
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import ru.null_checkers.common.use_cases.OpenUrl
import ru.null_checkers.common.use_cases_impls.OpenUrlUseCase
import ru.null_checkers.form_filling_screen.ui.formfilling.FormFilling.Companion.DIALOG_ABOUT_FORM

class DialogFactory {

    private val openUrl: OpenUrl = OpenUrlUseCase()

    fun createUrlOpenDialog(context: Context, url: String): AlertDialog.Builder {
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

    fun createAboutFormDialog(
        context: Context,
        preferences: SharedPreferences,
    ): AlertDialog.Builder {
        val checkBox = CheckBox(context).apply {
            text = "Больше не показывать"
            setPadding(32, 16, 32, 16)
        }

        return AlertDialog.Builder(context)
            .setTitle("Информация")
            .setMessage("При выборе заполнения с помощью онлайн формы, фотография, выбранная в приложении, не будет добавлена")
            .setView(checkBox)
            .setPositiveButton("Ок") { _, _ ->
                if (checkBox.isChecked) {
                    preferences.edit {
                        putBoolean(DIALOG_ABOUT_FORM, false)
                    }
                }
            }
    }
}
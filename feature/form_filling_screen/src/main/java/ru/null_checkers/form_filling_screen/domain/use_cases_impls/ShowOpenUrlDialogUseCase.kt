package ru.null_checkers.form_filling_screen.domain.use_cases_impls

import android.content.Context
import ru.null_checkers.form_filling_screen.domain.factory.DialogFactory
import ru.null_checkers.form_filling_screen.domain.use_cases.ShowOpenUrlDialog

class ShowOpenUrlDialogUseCase : ShowOpenUrlDialog {
    override fun invoke(context: Context, url: String) {
        val dialog = DialogFactory().createUrlOpenDialog(context, url)
        dialog.show()
    }
}
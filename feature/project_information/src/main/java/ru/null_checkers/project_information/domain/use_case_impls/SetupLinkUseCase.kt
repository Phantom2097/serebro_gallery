package ru.null_checkers.project_information.domain.use_case_impls

import android.view.View
import ru.null_checkers.common.use_cases.OpenUrl
import ru.null_checkers.common.use_cases_impls.OpenUrlUseCase
import ru.null_checkers.project_information.domain.use_case.SetupLink

class SetupLinkUseCase() : SetupLink {
    override fun invoke(view: View, url: String) {
        view.setOnClickListener {
            val openUrl : OpenUrl = OpenUrlUseCase()
            openUrl(view.context, url)
        }
    }
}
package ru.null_checkers.project_information.domain.use_case

import android.view.View

interface SetupLink {
    operator fun invoke(view: View, url: String)
}
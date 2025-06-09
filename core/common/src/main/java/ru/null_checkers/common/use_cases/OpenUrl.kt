package ru.null_checkers.common.use_cases

import android.content.Context

interface OpenUrl {
    operator fun invoke(context: Context, url: String)
}
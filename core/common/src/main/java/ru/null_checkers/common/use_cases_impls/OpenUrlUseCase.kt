package ru.null_checkers.common.use_cases_impls

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import ru.null_checkers.common.use_cases.OpenUrl

class OpenUrlUseCase : OpenUrl {
    override operator fun invoke(context: Context,url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        context.startActivity(intent)
    }
}
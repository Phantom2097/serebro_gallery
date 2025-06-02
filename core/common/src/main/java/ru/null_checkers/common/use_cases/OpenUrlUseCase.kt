package ru.null_checkers.common.use_cases

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.net.toUri

class OpenUrlUseCase {
    operator fun invoke(context: Context, view: View, url: String) {
        view.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            context.startActivity(intent)
        }
    }
}
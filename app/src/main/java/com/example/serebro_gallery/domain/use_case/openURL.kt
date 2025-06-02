package com.example.serebro_gallery.domain.use_case

import android.content.Context
import android.content.Intent
import android.net.Uri

fun openUrl(url: String, context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}
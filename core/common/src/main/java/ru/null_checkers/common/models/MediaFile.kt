package ru.null_checkers.common.models

import android.net.Uri

/**
 * @param uri поле, которое содержит uri изображения
 * @param name поле, которое содержит название изображения
 */
data class MediaFile(
    val uri: Uri,
    val name: String
)
package ru.null_checkers.form_filling_screen.ui.image_pick.adapter.callback

import androidx.recyclerview.widget.DiffUtil
import ru.null_checkers.common.models.Photo

class ImagePickDiffCallback : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(
        oldItem: Photo,
        newItem: Photo,
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Photo,
        newItem: Photo,
    ): Boolean {
        return oldItem.id == newItem.id && oldItem.isFavorite == newItem.isFavorite
    }
}
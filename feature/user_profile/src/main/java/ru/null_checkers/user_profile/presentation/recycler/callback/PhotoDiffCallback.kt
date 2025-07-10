package ru.null_checkers.user_profile.presentation.recycler.callback

import androidx.recyclerview.widget.DiffUtil
import ru.null_checkers.common.models.Photo

class PhotoDiffCallback : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem == newItem
    }
}
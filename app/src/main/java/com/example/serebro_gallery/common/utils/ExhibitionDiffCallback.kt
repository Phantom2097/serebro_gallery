package com.example.serebro_gallery.common.utils

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.example.serebro_gallery.domain.models.ExhibitionItem

class ExhibitionDiffCallback : DiffUtil.ItemCallback<ExhibitionItem>() {

    override fun areItemsTheSame(
        oldItem: ExhibitionItem, newItem: ExhibitionItem
    ): Boolean {
        if (oldItem.javaClass != newItem.javaClass) return false
        return oldItem.mainPhotoID == newItem.mainPhotoID
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: ExhibitionItem, newItem: ExhibitionItem
    ): Boolean {
        if (oldItem.javaClass != newItem.javaClass) return false
        return oldItem == newItem
    }
}
package com.example.serebro_gallery.common.utils

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.example.serebro_gallery.domain.models.PrizePhoto

class PrizeDiffCallback : DiffUtil.ItemCallback<PrizePhoto>() {

    override fun areItemsTheSame(
        oldItem: PrizePhoto, newItem: PrizePhoto
    ): Boolean {
        if (oldItem.javaClass != newItem.javaClass) return false
        return oldItem.imageID == newItem.imageID
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: PrizePhoto, newItem: PrizePhoto
    ): Boolean {
        if (oldItem.javaClass != newItem.javaClass) return false
        return oldItem == newItem
    }
}
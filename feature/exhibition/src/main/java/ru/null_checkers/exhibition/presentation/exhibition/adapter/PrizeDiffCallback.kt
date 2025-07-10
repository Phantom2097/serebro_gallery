package ru.null_checkers.exhibition.presentation.exhibition.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import ru.null_checkers.exhibition.models.PrizePhoto

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
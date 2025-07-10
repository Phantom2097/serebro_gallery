package ru.null_checkers.exhibition.presentation.exhibition_list.adapter.callback

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import ru.null_checkers.exhibition.models.ExhibitionItem

class ExhibitionDiffCallback : DiffUtil.ItemCallback<ExhibitionItem>() {

    override fun areItemsTheSame(
        oldItem: ExhibitionItem, newItem: ExhibitionItem,
    ): Boolean {
        if (oldItem.javaClass != newItem.javaClass) return false
        return oldItem.afisha == newItem.afisha
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: ExhibitionItem, newItem: ExhibitionItem,
    ): Boolean {
        if (oldItem.javaClass != newItem.javaClass) return false
        return oldItem == newItem
    }
}
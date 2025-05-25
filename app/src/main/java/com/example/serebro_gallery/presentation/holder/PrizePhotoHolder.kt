package com.example.serebro_gallery.presentation.holder

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.serebro_gallery.domain.models.PrizePhoto
import com.example.serebro_gallery.databinding.PrizePhotoItemBinding

class PrizePhotoHolder(viewItem: View) : RecyclerView.ViewHolder(viewItem) {
    val binding = PrizePhotoItemBinding.bind(viewItem)
    var itemNomination = binding.tvNomination
    val itemAuthor = binding.tvAuthorName
    var itemImage = binding.ivPrizeImage

    fun bind(item: PrizePhoto) {
        itemImage.setImageResource(item.imageID)
        itemNomination.text = item.nomination
        itemAuthor.text = "Автор: ${item.author}"
    }
}
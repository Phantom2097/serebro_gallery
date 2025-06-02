package com.example.serebro_gallery.presentation.holder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.serebro_gallery.R
import com.example.serebro_gallery.databinding.ExhibitionItemBinding
import com.example.serebro_gallery.domain.models.ExhibitionItem

class ExhibitionHolder(binding: ExhibitionItemBinding) : RecyclerView.ViewHolder(binding.root) {
    var itemDate = binding.tvItemDate
    val itemName = binding.tvItemName
    val itemDescription = binding.tvItemDescription
    var itemImage = binding.ivItemImage

    fun bind(item: ExhibitionItem) {
        //itemImage.setImageResource(item.mainPhotoID)
        itemDescription.text = item.description
        itemName.text = item.name
        itemDate.text = item.date

        Glide.with(itemView.context)
            .load(item.afisha)
            .error(R.drawable.logo_black)
            .into(itemImage)
    }
}
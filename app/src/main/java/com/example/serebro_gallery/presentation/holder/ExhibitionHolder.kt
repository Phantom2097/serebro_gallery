package com.example.serebro_gallery.presentation.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.serebro_gallery.databinding.ExhibitionItemBinding
import com.example.serebro_gallery.domain.models.ExhibitionItem
import com.example.serebro_gallery.domain.models.PrizePhoto

class ExhibitionHolder(viewItem: View) : RecyclerView.ViewHolder(viewItem) {
    val binding = ExhibitionItemBinding.bind(viewItem)
    var itemDate = binding.tvItemDate
    val itemName = binding.tvItemName
    val itemDescription = binding.tvItemDescription
    var itemImage = binding.ivItemImage

    fun bind(item: ExhibitionItem) {
        itemImage.setImageResource(item.mainPhotoID)
        itemDescription.text = item.description
        itemName.text = "«${item.name}»"
        itemDate.text = item.date
    }
}
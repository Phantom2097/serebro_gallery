package ru.null_checkers.exhibition.presentation.exhibition_list.adapter.view_holder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.null_checkers.exhibition.databinding.ExhibitionItemBinding
import ru.null_checkers.exhibition.models.ExhibitionItem
import ru.null_checkers.ui.R

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
            .error(R.drawable.logo_black_2)
            .into(itemImage)
    }
}
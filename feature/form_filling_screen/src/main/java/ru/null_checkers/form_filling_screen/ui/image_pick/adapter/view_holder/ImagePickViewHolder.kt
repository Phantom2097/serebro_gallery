package ru.null_checkers.form_filling_screen.ui.image_pick.adapter.view_holder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.null_checkers.common.models.Photo
import ru.null_checkers.form_filling_screen.databinding.FragmentImagePickItemBinding

class ImagePickViewHolder(private val binding: FragmentImagePickItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(photo: Photo) {
        Glide.with(binding.galleryItem)
            .load(photo.imagePath)
            .centerCrop()
            .into(binding.galleryItem)
    }
}
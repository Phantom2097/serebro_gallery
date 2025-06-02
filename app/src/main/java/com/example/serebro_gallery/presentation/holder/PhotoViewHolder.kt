package com.example.serebro_gallery.presentation.holder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.serebro_gallery.R
import com.example.serebro_gallery.domain.models.Photo

class PhotoViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val imageView: ImageView = itemView.findViewById(R.id.photoImageView)

    fun bind(photo: Photo) {
        Glide.with(itemView.context)
            .load(photo.imagePath)
            .centerCrop()
            .into(imageView)

    }
}
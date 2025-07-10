package ru.null_checkers.user_profile.presentation.recycler.view_holder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.null_checkers.common.models.Photo
import ru.null_checkers.user_profile.R

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
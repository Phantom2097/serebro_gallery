package ru.null_checkers.user_profile.presentation.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.null_checkers.common.models.Photo
import ru.null_checkers.user_profile.R
import ru.null_checkers.user_profile.presentation.recycler.callback.PhotoDiffCallback
import ru.null_checkers.user_profile.presentation.recycler.view_holder.PhotoViewHolder

class PhotoAdapter() :
    ListAdapter<Photo, PhotoViewHolder>(PhotoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount(): Int = currentList.size
}
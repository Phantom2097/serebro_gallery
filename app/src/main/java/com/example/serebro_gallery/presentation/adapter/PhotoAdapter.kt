package com.example.serebro_gallery.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.serebro_gallery.R
import com.example.serebro_gallery.common.utils.PhotoDiffCallback
import com.example.serebro_gallery.domain.models.Photo
import com.example.serebro_gallery.presentation.holder.PhotoViewHolder

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
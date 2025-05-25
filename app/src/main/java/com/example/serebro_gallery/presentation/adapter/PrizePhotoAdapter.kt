package com.example.serebro_gallery.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.serebro_gallery.common.utils.PrizeDiffCallback
import com.example.serebro_gallery.domain.models.PrizePhoto
import com.example.serebro_gallery.presentation.holder.PrizePhotoHolder
import com.example.serebro_gallery.R

class PrizePhotoAdapter() : ListAdapter<PrizePhoto, PrizePhotoHolder>(PrizeDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrizePhotoHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.prize_photo_item, parent, false
        )
        return PrizePhotoHolder(view)
    }


    override fun onBindViewHolder(holder: PrizePhotoHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
    }

    override fun getItemCount() = currentList.size

}
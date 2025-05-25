package com.example.serebro_gallery.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.serebro_gallery.R
import com.example.serebro_gallery.common.utils.ExhibitionDiffCallback
import com.example.serebro_gallery.domain.models.ExhibitionItem
import com.example.serebro_gallery.presentation.holder.ExhibitionHolder

class ExhibitionAdapter() : ListAdapter<ExhibitionItem, ExhibitionHolder>(ExhibitionDiffCallback()) {
    private var onItemClickListener: ((ExhibitionItem) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExhibitionHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.exhibition_item, parent, false
        )
        return ExhibitionHolder(view)
    }


    override fun onBindViewHolder(holder: ExhibitionHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(item)
        }
    }

    override fun getItemCount() = currentList.size

    fun setOnItemClickListener(listener: (ExhibitionItem) -> Unit) {
        onItemClickListener = listener
    }

}
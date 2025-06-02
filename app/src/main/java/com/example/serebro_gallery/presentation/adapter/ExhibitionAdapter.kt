package com.example.serebro_gallery.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.serebro_gallery.common.utils.ExhibitionDiffCallback
import com.example.serebro_gallery.databinding.ExhibitionItemBinding
import com.example.serebro_gallery.domain.models.ExhibitionItem
import com.example.serebro_gallery.presentation.holder.ExhibitionHolder

class ExhibitionAdapter() :
    ListAdapter<ExhibitionItem, ExhibitionHolder>(ExhibitionDiffCallback()) {

    private var onItemClickListener: ((ExhibitionItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExhibitionHolder {
        val binding = ExhibitionItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ExhibitionHolder(binding).apply {
            binding.root.setOnClickListener {
                val position = adapterPosition
                onItemClickListener?.invoke(currentList[position])
            }
        }
    }

    override fun onBindViewHolder(holder: ExhibitionHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun  getItemCount() = currentList.size

    fun setOnItemClickListener(listener: (ExhibitionItem) -> Unit) {
        onItemClickListener = listener
    }
}
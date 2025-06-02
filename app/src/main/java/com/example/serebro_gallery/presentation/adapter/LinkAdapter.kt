package com.example.serebro_gallery.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.serebro_gallery.R
import com.example.serebro_gallery.databinding.ItemLinkBinding
import com.example.serebro_gallery.domain.models.Link

class LinkAdapter(
    private val links: List<Link>,
    private val onClick: (Link) -> Unit
) : RecyclerView.Adapter<LinkAdapter.LinkViewHolder>() {

    class LinkViewHolder(binding: ItemLinkBinding) : RecyclerView.ViewHolder(binding.root) {
        val textView: TextView = itemView.findViewById(R.id.tv_link)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkViewHolder {
        val binding = ItemLinkBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LinkViewHolder(binding).apply {
            binding.root.setOnClickListener {
                val position = adapterPosition
                val link = links[position]
                onClick(link)
            }
        }
    }

    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {
        val link = links[position]
        holder.textView.text = link.title
    }

    override fun getItemCount() = links.size
}
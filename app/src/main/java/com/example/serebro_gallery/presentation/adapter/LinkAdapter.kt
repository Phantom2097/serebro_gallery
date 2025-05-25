package com.example.serebro_gallery.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.serebro_gallery.domain.models.Link
import com.example.serebro_gallery.R

class LinkAdapter(
    private val links: List<Link>,
    private val onClick: (Link) -> Unit
) : RecyclerView.Adapter<LinkAdapter.LinkViewHolder>() {

    class LinkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tv_link)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_link, parent, false)
        return LinkViewHolder(view)
    }

    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {
        val link = links[position]
        holder.textView.text = link.title
        holder.itemView.setOnClickListener { onClick(link) }
    }

    override fun getItemCount() = links.size
}
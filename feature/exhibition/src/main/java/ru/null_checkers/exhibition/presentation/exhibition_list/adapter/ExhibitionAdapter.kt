package ru.null_checkers.exhibition.presentation.exhibition_list.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.null_checkers.exhibition.presentation.exhibition_list.adapter.callback.ExhibitionDiffCallback
import ru.null_checkers.exhibition.presentation.exhibition_list.adapter.view_holder.ExhibitionHolder
import ru.null_checkers.exhibition.databinding.ExhibitionItemBinding
import ru.null_checkers.exhibition.models.ExhibitionItem

class ExhibitionAdapter() :
    ListAdapter<ExhibitionItem, ExhibitionHolder>(ExhibitionDiffCallback()) {

    private var onItemClickListener: ((ExhibitionItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExhibitionHolder {
        val binding = ExhibitionItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ExhibitionHolder(binding).apply {
            binding.root.setOnClickListener {
                val position = adapterPosition // RecyclerView.ViewHolder.getAdapterPosition
                Log.d("Exhibition", "go to detail")
                onItemClickListener?.invoke(currentList[position])
            }
        }
    }

    override fun onBindViewHolder(holder: ExhibitionHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount() = currentList.size

    fun setOnItemClickListener(listener: (ExhibitionItem) -> Unit) {
        onItemClickListener = listener
    }
}
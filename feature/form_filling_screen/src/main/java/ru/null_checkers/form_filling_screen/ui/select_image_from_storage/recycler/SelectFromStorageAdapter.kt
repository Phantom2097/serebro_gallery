package ru.null_checkers.form_filling_screen.ui.select_image_from_storage.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.null_checkers.form_filling_screen.databinding.SelectImageFromStorageItemBinding
import ru.null_checkers.form_filling_screen.ui.formfilling.MediaFile
import ru.null_checkers.form_filling_screen.ui.select_image_from_storage.recycler.callback.ImageDiffCallback
import ru.null_checkers.form_filling_screen.ui.select_image_from_storage.recycler.view_holders.ImageViewHolder

/**
 * @author Phantom2097
 */
class SelectFromStorageAdapter(
    private val onItemClick: OnItemClick
) : ListAdapter<MediaFile, ImageViewHolder>(ImageDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageViewHolder {
        val binding = SelectImageFromStorageItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ImageViewHolder(binding).apply {
            binding.root.setOnClickListener {
                val position = adapterPosition

                onItemClick.onItemClick(currentList[position])
            }
        }
    }

    override fun onBindViewHolder(
        holder: ImageViewHolder,
        position: Int
    ) {
        if (position != RecyclerView.NO_POSITION) {
            holder.bind(currentList[position])
        }
    }
}
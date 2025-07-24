package ru.null_checkers.form_filling_screen.ui.image_pick.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.null_checkers.common.models.Photo
import ru.null_checkers.common.shared_view_model.OnItemClick
import ru.null_checkers.form_filling_screen.databinding.FragmentImagePickItemBinding
import ru.null_checkers.form_filling_screen.domain.mappers.Mappers.toMediaFile
import ru.null_checkers.form_filling_screen.ui.image_pick.adapter.callback.ImagePickDiffCallback
import ru.null_checkers.form_filling_screen.ui.image_pick.adapter.view_holder.ImagePickViewHolder

class ImagePickAdapter(
    private val onItemClick: OnItemClick
) : ListAdapter<Photo, ImagePickViewHolder>(ImagePickDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ImagePickViewHolder {
        val binding =
            FragmentImagePickItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImagePickViewHolder(binding).apply {
            binding.root.setOnClickListener {
                val position = adapterPosition

                onItemClick.onItemClick(getItem(position).toMediaFile())
            }
        }
    }

    override fun onBindViewHolder(
        holder: ImagePickViewHolder,
        position: Int,
    ) {
        val photo = getItem(position)
        holder.bind(photo)
    }
}
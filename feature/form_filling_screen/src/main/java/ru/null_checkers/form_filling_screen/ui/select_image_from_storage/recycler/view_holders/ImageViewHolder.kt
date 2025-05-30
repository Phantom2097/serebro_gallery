package ru.null_checkers.form_filling_screen.ui.select_image_from_storage.recycler.view_holders

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.null_checkers.form_filling_screen.databinding.SelectImageFromStorageItemBinding
import ru.null_checkers.form_filling_screen.ui.formfilling.MediaFile

/**
 * @author Phantom2097
 */
class ImageViewHolder(
    private val binding: SelectImageFromStorageItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: MediaFile) = with(binding) {
        Log.d("ImageViewHolder", "Loading image: ${item.uri}")
        Glide.with(itemView.context)
            .load(item.uri)
            .override(500, 500)
            .centerCrop()
            .into(imageFromStorageItem)
    }
}
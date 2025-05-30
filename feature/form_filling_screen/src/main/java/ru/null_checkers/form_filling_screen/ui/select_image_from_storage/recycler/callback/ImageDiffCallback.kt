package ru.null_checkers.form_filling_screen.ui.select_image_from_storage.recycler.callback

import androidx.recyclerview.widget.DiffUtil
import ru.null_checkers.form_filling_screen.ui.formfilling.MediaFile

/**
 * @author Phantom2097
 */
class ImageDiffCallback : DiffUtil.ItemCallback<MediaFile>() {
    override fun areItemsTheSame(
        oldItem: MediaFile,
        newItem: MediaFile
    ): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(
        oldItem: MediaFile,
        newItem: MediaFile
    ): Boolean {
        return oldItem == newItem
    }
}
package com.example.serebro_gallery.presentation.swipe_feed

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.serebro_gallery.R
import com.example.serebro_gallery.databinding.FragmentSwipeFeedBinding
import com.example.serebro_gallery.domain.models.Photo
import com.example.serebro_gallery.domain.models.PhotoItem
import com.example.serebro_gallery.presentation.activity.MainActivity
import com.example.serebro_gallery.presentation.viewmodel.ExhibitionViewModel
import com.example.serebro_gallery.presentation.viewmodel.PhotoViewModel
import kotlinx.coroutines.launch

/**
 * Экран отображения фотографий с возможностью добавления их в избранное
 */
class SwipeFeedFragment : Fragment() {

    private var _binding: FragmentSwipeFeedBinding? = null
    val binding get() = _binding!!

    private lateinit var photoViewModel: PhotoViewModel
    private val exhibitionViewModel: ExhibitionViewModel by activityViewModels()

    /**
     * Индекс фотографии из списка
     */
    private var currentPhotoIndex = START_INDEX

    /**
     *  Список всех фотографий, который получается из photoViewModel
     */
    private var photoList: List<PhotoItem> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSwipeFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()

        initButtons()
        observeViewModel()
    }

    private fun initViewModel() {
        photoViewModel = (activity as MainActivity).getSharedPhotoViewModel()
    }

    private fun initButtons() = with(binding) {
        likeButton.setOnClickListener {
            photoViewModel.addItem(
                photoList[currentPhotoIndex].toPhoto()
            )
            showNextPhoto()
        }

        nextButton.setOnClickListener {
            if (currentPhotoIndex < photoList.size)
                showNextPhoto()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun changePhotoCounter(img: Int) {
        binding.photoCounter.text = "$img / ${photoList.size}"
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    exhibitionViewModel.photos.observe(viewLifecycleOwner) { photos ->
                        photoList = photos
                        showNextPhoto()
                    }
                }
            }
        }
    }

    private fun showCurrentPhoto() {
        if (photoList.isEmpty() || currentPhotoIndex !in photoList.indices) return

        val currentPhoto = photoList[currentPhotoIndex]

        Glide.with(this)
            .load(currentPhoto.link.toUri())
            .placeholder(R.drawable.empty_foto_field)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(binding.swipedImage)

        binding.imageAuthorName.text = currentPhoto.name

        changePhotoCounter(currentPhotoIndex + PHOTO_COUNTER_ADDITIONAL_NUM_FOR_INDEX)
    }

    /**
     *
     */
    private fun showNextPhoto() {
        if (photoList.isEmpty()) return

        currentPhotoIndex++

        if (currentPhotoIndex >= photoList.size) {
            showEndOfListDialog()
            return
        }

        showCurrentPhoto()

        binding.swipedImage.animate()
            .alpha(IMAGE_ALPHA_OFF)
            .setDuration(IMAGE_DURATION)
            .withEndAction {
                binding.swipedImage.alpha = IMAGE_ALPHA_ON
            }
            .start()
    }

    /**
     * Диалоговое окно отображается при достижении конца списка фотографий
     */
    private fun showEndOfListDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Конец галереи")
            .setMessage("Вы просмотрели все изображения. Что хотите сделать?")
            .setPositiveButton("Начать заново") { _, _ ->
                resetGallery()
            }
            .setNegativeButton("Выйти") { _, _ ->
                findNavController().popBackStack()
            }
            .setCancelable(false)
            .show()
    }

    /**
     * Просмотр списка фотографий заново
     */
    private fun resetGallery() {
        currentPhotoIndex = START_INDEX
        showNextPhoto()
    }

    private fun PhotoItem.toPhoto() = Photo(
        id = System.currentTimeMillis(),
        imagePath = this.link,
        isFavorite = true
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private companion object {
        private const val PHOTO_COUNTER_ADDITIONAL_NUM_FOR_INDEX = 1

        private const val START_INDEX = -1

        private const val IMAGE_ALPHA_ON = 1f
        private const val IMAGE_DURATION = 300L
        private const val IMAGE_ALPHA_OFF = 0f
    }
}
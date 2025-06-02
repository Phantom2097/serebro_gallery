package com.example.serebro_gallery.presentation.swipe_feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.serebro_gallery.databinding.FragmentSwipeFeedBinding
import com.example.serebro_gallery.domain.models.Photo
import com.example.serebro_gallery.domain.models.PhotoItem
import com.example.serebro_gallery.presentation.activity.MainActivity
import com.example.serebro_gallery.presentation.viewmodel.ExhibitionViewModel
import com.example.serebro_gallery.presentation.viewmodel.PhotoViewModel
import kotlinx.coroutines.launch

class SwipeFeedFragment : Fragment() {

    private var _binding: FragmentSwipeFeedBinding? = null
    val binding get() = _binding!!

    private lateinit var photoViewModel: PhotoViewModel
    private val exhibitionViewModel: ExhibitionViewModel by activityViewModels()

    private var currentPhotoIndex = 0
    private var photoList: List<PhotoItem> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSwipeFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photoViewModel = (activity as MainActivity).getSharedPhotoViewModel()

        initButtons()
        observeViewModel()
    }

    private fun initButtons() = with(binding) {
        likeButton.setOnClickListener {
            photoViewModel.addItem(
                photoList[currentPhotoIndex].toPhoto()
            )
            showNextPhoto()
        }

        nextButton.setOnClickListener {
            showNextPhoto()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            launch {
                exhibitionViewModel.photos.observe(viewLifecycleOwner) { photos ->
                    photoList = photos
                }
            }
        }
    }

    private fun showCurrentPhoto() {
        if (photoList.isEmpty() || currentPhotoIndex !in photoList.indices) return

        val currentPhoto = photoList[currentPhotoIndex]

        Glide.with(this)
            .load(currentPhoto.link.toUri())
            .into(binding.swipedImage)

        binding.imageAuthorName.text = currentPhoto.name
    }

    private fun showNextPhoto() {
        if (photoList.isEmpty()) return

        currentPhotoIndex = (currentPhotoIndex + 1) % photoList.size
        showCurrentPhoto()

        binding.swipedImage.animate()
            .alpha(0f)
            .setDuration(200)
            .withEndAction {
                binding.swipedImage.alpha = 1f
            }
            .start()
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
}
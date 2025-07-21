package ru.null_checkers.exhibition.presentation.swipe_feed

import android.R.attr.delay
import android.annotation.SuppressLint
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.null_checkers.common.entity.SharedViewModel
import ru.null_checkers.common.models.Photo
import ru.null_checkers.common.shared_view_model.PhotoSharedViewModel
import ru.null_checkers.exhibition.R
import ru.null_checkers.exhibition.databinding.FragmentSwipeFeedBinding
import ru.null_checkers.exhibition.models.PhotoItem
import ru.null_checkers.exhibition.presentation.exhibition.ExhibitionViewModel
import kotlin.math.abs

/**
 * Экран отображения фотографий с возможностью добавления их в избранное
 */
class SwipeFeedFragment : Fragment() {

    private var _binding: FragmentSwipeFeedBinding? = null
    val binding get() = _binding!!

    private lateinit var photoViewModel: PhotoSharedViewModel
    private val exhibitionViewModel: ExhibitionViewModel by activityViewModels()

    var arr_favorite = mutableSetOf<String>()

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
        photoViewModel = (activity as SharedViewModel).getSharedPhotoViewModel()
        photoViewModel.getFavorite()

//        for (i in photoViewModel.favorite.value){
//            arr_favorite.add(i.compId)
//        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initButtons() = with(binding) {
        likeButton.setOnClickListener {
            val curr = photoList[currentPhotoIndex].toPhoto(currentPhotoIndex)
            if (curr.compId in arr_favorite) {
                // Если уже в избранном - выполнить действие удаления
                heartImg.setImageResource(R.drawable.heart)
                photoViewModel.deleteFavorite(curr)
                arr_favorite.remove(curr.compId)
            } else {
                heartImg.setImageResource(R.drawable.heart_filled)
                photoViewModel.addItem(curr)
                arr_favorite.add(curr.compId)
            }
        }
        nextButton.setOnClickListener {
            if (currentPhotoIndex < photoList.size)
                showNextPhoto()
        }
        swipedImage.setOnTouchListener(object : View.OnTouchListener {
            private var x1: Float = 0f
            private var x2: Float = 0f
            private val MIN_DISTANCE = 150

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        x1 = event.x
                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        x2 = event.x
                        val deltaX = x2 - x1

                        if (abs(deltaX) > MIN_DISTANCE) {
                            if (deltaX > 0) {
                                // Свайп вправо - предыдущая фотография
                                showPreviousPhoto()
                            } else {
                                // Свайп влево - следующая фотография
                                showNextPhoto()
                            }
                        }
                        return true
                    }
                }
                return false
            }
        })
    }

    private fun showNextPhoto() {
        if (photoList.isEmpty()) return

        currentPhotoIndex++

        if (currentPhotoIndex >= photoList.size) {
            showEndOfListDialog()
            return
        }

        binding.swipedImage.animate()
            .translationXBy(-binding.swipedImage.width.toFloat())
            .alpha(IMAGE_ALPHA_OFF)
            .setDuration(IMAGE_DURATION)
            .withEndAction {
                binding.swipedImage.translationX = binding.swipedImage.width.toFloat()
                showCurrentPhoto()
                binding.swipedImage.animate()
                    .translationX(0f)
                    .alpha(IMAGE_ALPHA_ON)
                    .setDuration(IMAGE_DURATION)
                    .start()
            }
            .start()
    }

    private fun showPreviousPhoto() {
        if (photoList.isEmpty() || currentPhotoIndex <= 0) return

        currentPhotoIndex--

        binding.swipedImage.animate()
            .translationXBy(binding.swipedImage.width.toFloat())
            .alpha(IMAGE_ALPHA_OFF)
            .setDuration(IMAGE_DURATION)
            .withEndAction {
                binding.swipedImage.translationX = -binding.swipedImage.width.toFloat()
                showCurrentPhoto()
                binding.swipedImage.animate()
                    .translationX(0f)
                    .alpha(IMAGE_ALPHA_ON)
                    .setDuration(IMAGE_DURATION)
                    .start()
            }
            .start()
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
                launch {
                    photoViewModel.favorite.collect { favorites ->
                        arr_favorite.clear()
                        arr_favorite.addAll(favorites.map { it.compId })
                        if (photoList.isNotEmpty() && currentPhotoIndex in photoList.indices) {
                            showCurrentPhoto()
                        }
                    }
                }
            }
        }
    }

    private fun showCurrentPhoto() {
        if (photoList.isEmpty() || currentPhotoIndex !in photoList.indices) return

        val currentPhoto = photoList[currentPhotoIndex]
        if (currentPhoto.toPhoto(currentPhotoIndex).compId in arr_favorite) {
            binding.heartImg.setImageResource(R.drawable.heart_filled)
        } else {
            binding.heartImg.setImageResource(R.drawable.heart)
        }

        Glide.with(this)
            .load(currentPhoto.imageLink.toUri())
            //.placeholder(R.drawable.empty_foto_field)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(binding.swipedImage)

        binding.imageAuthorName.text = currentPhoto.author

        changePhotoCounter(currentPhotoIndex + PHOTO_COUNTER_ADDITIONAL_NUM_FOR_INDEX)
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

    private fun PhotoItem.toPhoto(index: Int) = Photo(
        id = System.currentTimeMillis(),
        imagePath = this.imageLink,
        isFavorite = true,
        compId = exhibitionViewModel.exhibitionName + index.toString()
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
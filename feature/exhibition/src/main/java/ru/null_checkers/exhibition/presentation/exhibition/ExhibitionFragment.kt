package ru.null_checkers.exhibition.presentation.exhibition

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import ru.null_checkers.exhibition.R
import ru.null_checkers.exhibition.databinding.FragmentExhibitionBinding
import ru.null_checkers.exhibition.domain.CheckDeadline
import ru.null_checkers.exhibition.presentation.exhibition_list.MainViewModel
import ru.null_checkers.exhibition.presentation.exhibition_list.state.AllPhotosState
import ru.null_checkers.exhibition.presentation.exhibition_list.state.PrizePhotoState
import ru.null_checkers.ui.toolbar.ToolbarController

class ExhibitionFragment : Fragment(R.layout.fragment_exhibition) {
    private lateinit var binding: FragmentExhibitionBinding
    val mainViewModel: MainViewModel by activityViewModels()
    private val exhibitionViewModel by activityViewModels<ExhibitionViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        redefineOnBackPressed()

        binding = FragmentExhibitionBinding.bind(view)

        val exhibition = mainViewModel.currExhibition.value
        binding.tvTitle.text = exhibition?.name

        setupTitle()

        Glide.with(this)
            .load(exhibition?.afisha)
            //.placeholder(ru.null_checkers.ui.R.drawable.logo_black_2)
            .error(ru.null_checkers.ui.R.drawable.logo_black_2)
            .into(binding.ivMainPhoto)
        println("${exhibition?.afisha}")

        exhibitionViewModel.loadPhotos(exhibition?.link, exhibition?.name ?: "Unknown")

        exhibitionViewModel.description.observe(viewLifecycleOwner) { text ->
            if (text.isNullOrEmpty()) {
                binding.aboutExhib.text = exhibition?.description
            } else {
                binding.aboutExhib.text = text
            }
        }

        val showButton = CheckDeadline.checkDeadline(exhibition?.description)
        binding.deadlineButton.visibility = if (showButton) View.VISIBLE else View.GONE
        if (showButton) {
            binding.deadlineButton.setOnClickListener {
                findNavController().navigate(R.id.to_form_filling)
            }
        } else {
            binding.deadlineButton.setOnClickListener(null)
        }

        exhibitionViewModel.statePrize.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PrizePhotoState.Success -> {
                    val placeholder = ru.null_checkers.ui.R.drawable.logo_black_2
                    Glide.with(this)
                        .load(state.exhibitions.imageLink)
                        .error(placeholder)
                        .into(binding.ivFirstPlace)

                    binding.tvFirstPlace.visibility = View.VISIBLE
                    binding.ivFirstPlace.visibility = View.VISIBLE
                    binding.tvFirstPlaceAuthor.visibility = View.VISIBLE
                    binding.tvFirstPlaceAuthor.text = state.exhibitions.author
                }

                is PrizePhotoState.Error -> {
                    binding.tvFirstPlace.visibility = View.GONE
                    binding.ivFirstPlace.visibility = View.GONE
                    binding.tvFirstPlaceAuthor.visibility = View.GONE
                }

                else -> {}
            }
        }

        exhibitionViewModel.stateAllPhotos.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AllPhotosState.Success -> {
                    binding.swipeFeedButton.visibility = View.VISIBLE
                }
                is AllPhotosState.Error -> {
                    binding.swipeFeedButton.visibility = View.GONE
                }
                else -> {}
            }
        }

        binding.swipeFeedButton.setOnClickListener {
            findNavController().navigate(R.id.action_exhibitionFragment_to_swipeFeedFragment)
        }

    }

    private fun redefineOnBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.mainFragment, false)
                mainViewModel.clearSelectedItem()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun setupTitle() {
        (requireActivity() as? ToolbarController)?.setTitle(
            getString(R.string.exhibitionFragmentTitle)
        )
    }
}
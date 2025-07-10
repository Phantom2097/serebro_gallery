package ru.null_checkers.exhibition.presentation.exhibition

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import ru.null_checkers.exhibition.R
import ru.null_checkers.exhibition.databinding.FragmentExhibitionBinding
import ru.null_checkers.exhibition.presentation.exhibition_list.MainViewModel
import ru.null_checkers.ui.toolbar.ToolbarController

class ExhibitionFragment : Fragment(R.layout.fragment_exhibition) {
    private lateinit var binding: FragmentExhibitionBinding
    val mainViewModel: MainViewModel by activityViewModels()
    private val photoViewModel by activityViewModels<ExhibitionViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        redefineOnBackPressed()

        binding = FragmentExhibitionBinding.bind(view)

        val exhibition = mainViewModel.currExhibition.value
        binding.tvTitle.text = exhibition?.name

        setupTitle()

        Glide.with(this)
            .load(exhibition?.afisha)
            .placeholder(ru.null_checkers.ui.R.drawable.logo_black_2)
            .error(ru.null_checkers.ui.R.drawable.logo_black_2)
            .into(binding.ivMainPhoto)

        binding.tvDescription.text = exhibition?.description

        photoViewModel.loadPhotos(exhibition?.link)

        photoViewModel.prizephoto.observe(viewLifecycleOwner) { photo ->
            val placeholder = ru.null_checkers.ui.R.drawable.logo_black_2

            val imageToLoad = when {
                photo?.imageID.isNullOrEmpty() -> placeholder
                else -> photo.imageID
            }

            Glide.with(this)
                .load(imageToLoad)
                .error(placeholder)
                .into(binding.ivFirstPlace)
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
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun setupTitle() {
        (requireActivity() as? ToolbarController)?.setTitle(
            getString(R.string.exhibitionFragmentTitle)
        )
    }
}
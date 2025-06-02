package com.example.serebro_gallery.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.serebro_gallery.R
import com.example.serebro_gallery.databinding.FragmentExhibitionBinding
import com.example.serebro_gallery.presentation.viewmodel.ExhibitionViewModel
import com.example.serebro_gallery.presentation.viewmodel.MainViewModel

class ExhibitionFragment : Fragment(R.layout.fragment_exhibition) {
    private lateinit var binding: FragmentExhibitionBinding
    val mainViewModel: MainViewModel by activityViewModels()
    private val photoViewModel by activityViewModels<ExhibitionViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentExhibitionBinding.bind(view)

        val exhibition = mainViewModel.currExhibition.value
        binding.tvTitle.setText(exhibition?.name)
        Glide.with(this)
            .load(exhibition?.afisha)
            .placeholder(R.drawable.logo_black)
            .error(R.drawable.logo_black)
            .into(binding.ivMainPhoto)

        binding.tvDescription.setText(exhibition?.description)

        photoViewModel.loadPhotos(exhibition?.link)

        photoViewModel.prizephoto.observe(viewLifecycleOwner) { photo ->
            val placeholder = R.drawable.logo_black

            val imageToLoad = when {
                photo?.imageID.isNullOrEmpty() -> placeholder
                else -> photo.imageID
            }

            Glide.with(this)
                .load(imageToLoad)
                .error(placeholder)
                .into(binding.ivFirstPlace)
        }

    }
}
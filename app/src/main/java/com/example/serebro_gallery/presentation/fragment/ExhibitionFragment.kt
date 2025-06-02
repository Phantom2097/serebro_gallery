package com.example.serebro_gallery.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.serebro_gallery.R
import com.example.serebro_gallery.databinding.FragmentExhibitionBinding
import com.example.serebro_gallery.domain.models.PrizePhoto
import com.example.serebro_gallery.presentation.activity.MainActivity
import com.example.serebro_gallery.presentation.adapter.PrizePhotoAdapter
import com.example.serebro_gallery.presentation.viewmodel.ExhibitionViewModel
import com.example.serebro_gallery.presentation.viewmodel.MainViewModel

class ExhibitionFragment : Fragment(R.layout.fragment_exhibition) {
    private lateinit var binding: FragmentExhibitionBinding
    private lateinit var adapter: PrizePhotoAdapter
    val mainViewModel: MainViewModel by activityViewModels()
    private val photoViewModel by activityViewModels<ExhibitionViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentExhibitionBinding.bind(view)
        val recyclerView = binding.rcvPrizePhoto
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = PrizePhotoAdapter()
        recyclerView.adapter = adapter

        init()

        val exhibition = mainViewModel.currExhibition.value
        binding.tvTitle.text = exhibition?.name

        (requireActivity() as? MainActivity)?.updateToolbarTitle("Выставка")

        Glide.with(this)
            .load(exhibition?.afisha)
            .placeholder(R.drawable.logo_black)
            .error(R.drawable.logo_black)
            .into(binding.ivMainPhoto)
        println("!!! ${exhibition?.afisha}")
        binding.tvDescription.text = exhibition?.description
        binding.ivFirstPlace.setImageResource(R.drawable.photo)

        photoViewModel.loadPhotos(exhibition?.link)
        photoViewModel.photos.observe(viewLifecycleOwner) { photo ->

            photo.forEach {
                println("""
                   Автор: ${it.name}
                   Ссылка фото: ${it.link}
                   ${"-".repeat(50)}
               """.trimIndent())
            }

        }

        binding.swipeFeedButton.setOnClickListener {
            findNavController().navigate(R.id.action_exhibitionFragment_to_swipeFeedFragment)
        }
    }

    private fun init() {
        val initList = listOf<PrizePhoto>(
            PrizePhoto("Второе место", "Юлия Першина", R.drawable.photo),
            PrizePhoto("Третье место", "Андрей Соболев", R.drawable.photo),
            PrizePhoto("Спецприз", "Владислав Зарудний", R.drawable.photo),
            PrizePhoto("Спецприз", "Александр Махоткин", R.drawable.photo)
        )

        adapter.submitList(initList)

    }
}
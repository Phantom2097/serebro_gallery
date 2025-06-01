package com.example.serebro_gallery.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.serebro_gallery.R
import com.example.serebro_gallery.databinding.FragmentExhibitionBinding
import com.example.serebro_gallery.domain.models.PrizePhoto
import com.example.serebro_gallery.presentation.activity.MainActivity
import com.example.serebro_gallery.presentation.adapter.PrizePhotoAdapter
import com.example.serebro_gallery.presentation.viewmodel.MainViewModel

class ExhibitionFragment : Fragment(R.layout.fragment_exhibition) {
    private lateinit var binding: FragmentExhibitionBinding
    private lateinit var adapter: PrizePhotoAdapter
    val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentExhibitionBinding.bind(view)
        val recyclerView = binding.rcvPrizePhoto
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = PrizePhotoAdapter()
        recyclerView.adapter = adapter

        init()

        val exhibition = mainViewModel.currExhibition.value
        binding.tvTitle.setText(exhibition?.name)

        (requireActivity() as? MainActivity)?.updateToolbarTitle(exhibition?.name ?: "Выставка")

        Glide.with(this)
            .load(exhibition?.afisha)
            .placeholder(R.drawable.logo_black)
            .error(R.drawable.logo_black)
            .into(binding.ivMainPhoto)
        println("!!! ${exhibition?.afisha}")
        binding.tvDescription.setText(exhibition?.description)
        binding.ivFirstPlace.setImageResource(R.drawable.photo)
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
package com.example.serebro_gallery.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.serebro_gallery.R
import com.example.serebro_gallery.databinding.FragmentExhibitionBinding
import com.example.serebro_gallery.domain.models.PrizePhoto
import com.example.serebro_gallery.presentation.adapter.PrizePhotoAdapter
import com.example.serebro_gallery.presentation.viewmodel.MainViewModel
import kotlin.getValue

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
        val item = mainViewModel.currExhibition.value
        binding.tvTitle.setText(item?.name)
        binding.ivFirstPlace.setImageResource(R.drawable.photo)
        binding.tvDescription.setText(item?.description)
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
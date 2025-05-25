package com.example.serebro_gallery.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.serebro_gallery.R
import com.example.serebro_gallery.databinding.FragmentMainBinding
import com.example.serebro_gallery.domain.models.ExhibitionItem
import com.example.serebro_gallery.domain.models.PrizePhoto
import com.example.serebro_gallery.presentation.adapter.ExhibitionAdapter
import com.example.serebro_gallery.presentation.adapter.PrizePhotoAdapter
import com.example.serebro_gallery.presentation.viewmodel.MainViewModel
import kotlin.getValue

class MainFragment : Fragment(R.layout.fragment_main) {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ExhibitionAdapter
    val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = ExhibitionAdapter().apply {
            setOnItemClickListener { item ->
                handleItemClick(item)
            }
        }
        binding.rcvExhibition.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@MainFragment.adapter
        }

        loadData()
        setupObserver()
    }

    private fun loadData() {
        val items = listOf(
            ExhibitionItem(
                "22 января 2023",
                "Не зря тащил фотик в такую даль",
                "Первая фотовыставка в 2023 году.",
                R.drawable.img
            ),
            ExhibitionItem(
                "27 ноября 2022",
                "Абстракция",
                "Первая фотовыставка в особняке «Едва знакомы»",
                R.drawable.img
            ),
            ExhibitionItem(
                "24 декабря 2022",
                "Лучший кадр 2022",
                "Новогодняя фотовыставка",
                R.drawable.img
            ),
            ExhibitionItem(
                "26 февраля 2023",
                "Кадр, в который хочется вернуться",
                "Первая фотовыставка в secret.gallery на Красном Октябре",
                R.drawable.img
            )
        )

        adapter.submitList(items)
    }

    private fun handleItemClick(item: ExhibitionItem) {
        viewModel.selectItem(item)

    }

    private fun setupObserver() {
        viewModel.selectedItem.observe(viewLifecycleOwner) { item ->
            item?.let {
                if (findNavController().currentBackStackEntry?.destination?.id == R.id.mainFragment) {
                    viewModel.setExhibition(item)
                    findNavController().navigate(R.id.action_mainFragment_to_exhibitionFragment)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}
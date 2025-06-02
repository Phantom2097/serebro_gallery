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
                viewModel.selectItem(item)
            }
        }

        binding.rcvExhibition.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@MainFragment.adapter
        }
        setupObserver()

        if (viewModel.exhibitions.value.isNullOrEmpty()) {
            println("!!! loading")
            viewModel.loadExhibitions()
        }
    }

    private fun setupObserver() {
        viewModel.exhibitions.observe(viewLifecycleOwner) { exhibitions ->
            exhibitions?.let {
                adapter.submitList(it)
            }

            exhibitions.forEach {
                println("""
                   Выставка: ${it.name}
                   Дата: ${it.date}
                   Описание: ${it.description.take(50)}...
                   ID фото: ${it.afisha}
                   ${"-".repeat(50)}
               """.trimIndent())
            }

        }

        viewModel.currExhibition.observe(viewLifecycleOwner) { item ->
            item?.let {
                if (findNavController().currentBackStackEntry?.destination?.id == R.id.mainFragment) {
                    viewModel.selectItem(item)
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
package com.example.serebro_gallery.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.serebro_gallery.R
import com.example.serebro_gallery.databinding.FragmentMainBinding
import com.example.serebro_gallery.presentation.activity.MainActivity
import com.example.serebro_gallery.presentation.adapter.ExhibitionAdapter
import com.example.serebro_gallery.presentation.viewmodel.MainViewModel

class MainFragment : Fragment(R.layout.fragment_main) {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ExhibitionAdapter
    private val viewModel: MainViewModel by activityViewModels()

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

        (requireActivity() as? MainActivity)?.updateToolbarTitle("Выставки")

        binding.rcvExhibition.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@MainFragment.adapter
        }
        setupObserver()

        if (viewModel.exhibitions.value.isNullOrEmpty()) {
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

//    companion object {
//        fun newInstance() = MainFragment()
//    }
}
package com.example.serebro_gallery.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.serebro_gallery.R
import com.example.serebro_gallery.databinding.FragmentMainBinding
import com.example.serebro_gallery.domain.models.ExhibitionItem
import com.example.serebro_gallery.domain.models.PrizePhoto
import com.example.serebro_gallery.presentation.adapter.ExhibitionAdapter
import com.example.serebro_gallery.presentation.viewmodel.ExhibitionsState
import com.example.serebro_gallery.presentation.viewmodel.MainViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlin.getValue

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

        val shimmerLayout = view.findViewById<ShimmerFrameLayout>(R.id.shimmerLayout)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rcv_exhibition)

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
            viewModel.loadExhibitions()
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ExhibitionsState.Loading -> {
                    shimmerLayout.startShimmer()
                    shimmerLayout.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
                is ExhibitionsState.Success -> {
                    shimmerLayout.stopShimmer()
                    shimmerLayout.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE

                    adapter.submitList(state.exhibitions)

                    state.exhibitions.forEach {
                        println("""
                            Выставка: ${it.name}
                            Дата: ${it.date}
                            Описание: ${it.description.take(50)}...
                            ID фото: ${it.afisha}
                            ${"-".repeat(50)}
                        """.trimIndent())
                    }
                }
                is ExhibitionsState.Error -> {
                    val ops = view.findViewById<TextView>(R.id.ops)
                    shimmerLayout.stopShimmer()
                    recyclerView.visibility = View.GONE
                    ops.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupObserver() {
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
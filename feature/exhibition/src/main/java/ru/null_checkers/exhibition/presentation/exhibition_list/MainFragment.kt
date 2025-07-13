package ru.null_checkers.exhibition.presentation.exhibition_list

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import ru.null_checkers.exhibition.R
import ru.null_checkers.exhibition.databinding.FragmentMainBinding
import ru.null_checkers.exhibition.presentation.exhibition_list.adapter.ExhibitionAdapter
import ru.null_checkers.exhibition.presentation.exhibition_list.state.ExhibitionsState
import ru.null_checkers.ui.toolbar.ToolbarController

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ExhibitionAdapter
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
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
                Log.d("Exhibition", "go to detail")
                viewModel.selectItem(item)
            }
        }

        setupTitle()
        setupRefresh()

        binding.rcvExhibition.apply {
            layoutManager = setupLayoutManagerByWidth()
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

                    state.exhibitions.forEach { (date, name, description, afisha) ->
                        println(
                            """
                            Выставка: $name
                            Дата: $date
                            Описание: ${description.take(50)}...
                            ID фото: $afisha
                            ${"-".repeat(50)}
                        """.trimIndent()
                        )
                    }

                    binding.swipeRefresh.isRefreshing = false
                }

                is ExhibitionsState.Error -> {
                    val ops = view.findViewById<TextView>(R.id.ops)
                    shimmerLayout.stopShimmer()
                    recyclerView.visibility = View.GONE
                    ops.visibility = View.VISIBLE

                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }
    }

    /**
     * Получение LayoutManager в зависимости от ширины дисплея устройства
     */
    private fun setupLayoutManagerByWidth(): LayoutManager {
        val displayWidth = getScreenWidthInDp(requireActivity())

        Log.d("CurrentWidth", "Ширина: $displayWidth")

        val gridSpan = gridSpanByWidth(displayWidth)

        return GridLayoutManager(requireContext(), gridSpan)
    }

    /**
     * Получение ширины дисплея
     */
    private fun getScreenWidthInDp(activity: Activity): Int = with(activity) {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getScreenWidthInDpApi30Plus(this@with)
        } else {
            resources.configuration.screenWidthDp
        }
    }

    /**
     * Получение ширины дисплея API 30+
     */
    @RequiresApi(Build.VERSION_CODES.R)
    private fun getScreenWidthInDpApi30Plus(activity: Activity): Int = with(activity) {
        val windowMetrics = windowManager.currentWindowMetrics
        val bounds = windowMetrics.bounds
        return (bounds.width() / resources.displayMetrics.density).toInt()
    }

    /**
     * Определение размера сетки
     */
    private fun gridSpanByWidth(displayWidth: Int): Int = when {
        displayWidth <= WIDTH_MEDIUM -> LAYOUT_MANAGER_GRID_MEDIUM
        displayWidth <= WIDTH_EXPANDED -> LAYOUT_MANAGER_GRID_EXPANDED
        displayWidth <= WIDTH_LARGE -> LAYOUT_MANAGER_GRID_LARGE
        else -> LAYOUT_MANAGER_GRID_EXTRA_LARGE
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

    /**
     * Перезагрузка страницы
     */
    private fun setupRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            Log.d("Refresh", "Call Refresh")
            viewModel.loadExhibitions()
        }
    }

    private fun setupTitle() {
        (requireActivity() as? ToolbarController)?.setTitle(
            getString(R.string.mainFragmentTitle)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private companion object {
        private const val WIDTH_MEDIUM = 600
        private const val WIDTH_EXPANDED = 1200
        private const val WIDTH_LARGE = 1600

        private const val LAYOUT_MANAGER_GRID_MEDIUM = 1
        private const val LAYOUT_MANAGER_GRID_EXPANDED = 2
        private const val LAYOUT_MANAGER_GRID_LARGE = 3
        private const val LAYOUT_MANAGER_GRID_EXTRA_LARGE = 4
    }
}
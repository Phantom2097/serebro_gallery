package ru.null_checkers.user_profile.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ru.null_checkers.common.entity.SharedViewModel
import ru.null_checkers.common.shared_view_model.PhotoSharedViewModel
import ru.null_checkers.user_profile.R
import ru.null_checkers.user_profile.presentation.recycler.adapter.PhotoAdapter

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: PhotoSharedViewModel
    private lateinit var adapter: PhotoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as SharedViewModel).getSharedPhotoViewModel()
        recyclerView = view.findViewById(R.id.rcv_favorite)
        adapter = PhotoAdapter()
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.adapter = adapter

        viewModel.getFavorite()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favorite.collect {
                    adapter.submitList(it)
                }
            }
        }
    }

    companion object {
        fun newInstance(): FavoriteFragment {
            return FavoriteFragment()
        }
    }
}

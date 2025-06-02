package ru.null_checkers.user_profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.serebro_gallery.presentation.activity.MainActivity
import kotlinx.coroutines.launch
import ru.null_checkers.common.setup_view_model.PhotoViewModel
import ru.null_checkers.user_profile.recycler.PhotoAdapter

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: PhotoViewModel
    private lateinit var adapter: PhotoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).getSharedPhotoViewModel()
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

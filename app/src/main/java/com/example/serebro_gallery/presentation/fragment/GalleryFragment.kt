package com.example.serebro_gallery.presentation.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.serebro_gallery.R
import com.example.serebro_gallery.common.utils.LoadFromLocalStorage
import com.example.serebro_gallery.data.AppDatabase
import com.example.serebro_gallery.databinding.FragmentGalleryBinding
import com.example.serebro_gallery.domain.models.Photo
import com.example.serebro_gallery.domain.repository.PhotoRepository
import com.example.serebro_gallery.presentation.activity.MainActivity
import com.example.serebro_gallery.presentation.adapter.PhotoAdapter
import com.example.serebro_gallery.presentation.viewmodel.PhotoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class GalleryFragment : Fragment(R.layout.fragment_gallery) {
    private lateinit var adapter: PhotoAdapter
    private lateinit var rcvGallery: RecyclerView
    private lateinit var addBtn: Button
    private lateinit var viewModel: PhotoViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).getSharedPhotoViewModel()
        adapter = PhotoAdapter()
        rcvGallery = view.findViewById(R.id.rcv_gallery)
        addBtn = view.findViewById(R.id.AddPhotoBtn)
        rcvGallery.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = this@GalleryFragment.adapter
        }
        val load = LoadFromLocalStorage(this, viewModel)
        load.addImageFromLocal(addBtn)
        viewModel.getGalleryPhotos()
        viewModel.currPhoto.observe(viewLifecycleOwner) { photo ->
            viewModel.addItem(photo)
            viewModel.getGalleryPhotos()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    adapter.submitList(it)
                }
            }
        }
    }

    companion object{
        fun newInstance() : GalleryFragment {
            return GalleryFragment()
        }
    }
}
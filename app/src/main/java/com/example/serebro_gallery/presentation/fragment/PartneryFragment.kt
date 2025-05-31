package com.example.serebro_gallery.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.serebro_gallery.R
import com.example.serebro_gallery.presentation.viewmodel.PartnersViewModel

class PartneryFragment : Fragment() {

    val viewModel: PartnersViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_partnery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.partners.value.isNullOrEmpty()) {
            println("!!! loading")
            viewModel.loadPartners()
        }
    }
}
package com.example.serebro_gallery.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.serebro_gallery.R
import com.example.serebro_gallery.databinding.FragmentLinkBinding
import com.example.serebro_gallery.domain.models.Link
import com.example.serebro_gallery.presentation.adapter.LinkAdapter

class LinkFragment : Fragment() {
    private var _binding: FragmentLinkBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLinkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val links = listOf(
            Link("Главная", R.id.mainFragment),
            Link("Партнёры", R.id.partnersFragment),
            Link("Контакты", R.id.mainFragment),
            Link("Профиль", R.id.profileFragment)
        )

        binding.rvLinks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = LinkAdapter(links) { link ->
                findNavController().navigate(link.destinationId)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
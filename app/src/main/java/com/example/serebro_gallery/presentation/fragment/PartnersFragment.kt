package com.example.serebro_gallery.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.serebro_gallery.R
import ru.null_checkers.ui.toolbar.ToolbarController

class PartnersFragment : Fragment(R.layout.fragment_partners) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTitle()
    }

    private fun setupTitle() {
        (requireActivity() as? ToolbarController)?.setTitle("Партнёры и друзья")
    }
}
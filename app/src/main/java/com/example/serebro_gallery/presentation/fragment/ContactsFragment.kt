package com.example.serebro_gallery.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.serebro_gallery.R
import com.example.serebro_gallery.domain.use_case.openUrl
import ru.null_checkers.ui.toolbar.ToolbarController

class ContactsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTitle()

        val llVk = view.findViewById<LinearLayout>(R.id.ll_vk)
        val llTelegram = view.findViewById<LinearLayout>(R.id.ll_tg)
        val llDzen = view.findViewById<LinearLayout>(R.id.ll_dz)

        val urlVk = "https://vk.com/serebro.gallery"
        val urlTelegram = "https://t.me/serebro_gallery"
        val urlDzen = "https://dzen.ru/serebro_gallery"

        llVk.setOnClickListener {
            openUrl(urlVk, requireContext())
        }
        llTelegram.setOnClickListener {
            openUrl(urlTelegram, requireContext())
        }
        llDzen.setOnClickListener {
            openUrl(urlDzen, requireContext())
        }
    }

    private fun setupTitle() {
        (requireActivity() as? ToolbarController)?.setTitle("Контакты")
    }
}
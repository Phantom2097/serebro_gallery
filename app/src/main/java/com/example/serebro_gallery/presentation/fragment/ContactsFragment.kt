package com.example.serebro_gallery.presentation.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.example.serebro_gallery.R

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

        val llVk = view.findViewById<LinearLayout>(R.id.ll_vk)
        val llTelegram = view.findViewById<LinearLayout>(R.id.ll_tg)
        val llDzen = view.findViewById<LinearLayout>(R.id.ll_dz)

        val urlVk = "https://vk.com/serebro.gallery"
        val urlTelegram = "https://t.me/serebro_gallery"
        val urlDzen = "https://dzen.ru/serebro_gallery"

        fun openUrl(url: String) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        llVk.setOnClickListener {
            openUrl(urlVk)
        }
        llTelegram.setOnClickListener {
            openUrl(urlTelegram)
        }
        llDzen.setOnClickListener {
            openUrl(urlDzen)
        }
    }
}
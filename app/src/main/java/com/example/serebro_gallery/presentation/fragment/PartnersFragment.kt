package com.example.serebro_gallery.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import androidx.fragment.app.Fragment
import com.example.serebro_gallery.R
import com.example.serebro_gallery.domain.use_case.openUrl

class PartnersFragment : Fragment(R.layout.fragment_partners) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_partners, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val llSkanim = view.findViewById<ConstraintLayout>(R.id.ll_skanim)
        val llIpfl = view.findViewById<ConstraintLayout>(R.id.ll_ipfl)
        val llWfolio = view.findViewById<ConstraintLayout>(R.id.ll_wfolio)
        val llMStore = view.findViewById<ConstraintLayout>(R.id.ll_mstore)

        val urlSkanim = "https://xn--80aqekdv.xn--p1ai/"
        val urlWfolio = "https://wfolio.ru/"
        val urlIpfl = "https://mipap.ru/"
        val urlMstore = "https://vk.com/mylnitsa_store"

        llSkanim.setOnClickListener {
            openUrl(urlSkanim, requireContext())
        }
        llIpfl.setOnClickListener {
            openUrl(urlIpfl, requireContext())
        }
        llWfolio.setOnClickListener {
            openUrl(urlWfolio, requireContext())
        }
        llMStore.setOnClickListener {
            openUrl(urlMstore, requireContext())
        }
    }
}
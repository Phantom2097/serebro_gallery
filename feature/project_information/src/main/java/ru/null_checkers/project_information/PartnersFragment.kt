package ru.null_checkers.project_information

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import ru.null_checkers.common.use_cases.OpenUrlUseCase
import ru.null_checkers.ui.toolbar.ToolbarController

class PartnersFragment : Fragment(R.layout.fragment_partners) {

    private val openUrl = OpenUrlUseCase()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTitle()

        val llSkanim = view.findViewById<ConstraintLayout>(R.id.ll_skanim)
        val llIpfl = view.findViewById<ConstraintLayout>(R.id.ll_ipfl)
        val llWfolio = view.findViewById<ConstraintLayout>(R.id.ll_wfolio)
        val llMStore = view.findViewById<ConstraintLayout>(R.id.ll_mstore)

        val urlSkanim = "https://xn--80aqekdv.xn--p1ai/"
        val urlIpfl = "https://mipap.ru/"
        val urlWfolio = "https://wfolio.ru/"
        val urlMstore = "https://vk.com/mylnitsa_store"

        requireContext().apply {
            openUrl(this, llSkanim, urlSkanim)
            openUrl(this, llIpfl, urlIpfl)
            openUrl(this, llWfolio, urlWfolio)
            openUrl(this, llMStore, urlMstore)
        }
    }

    private fun setupTitle() {
        (requireActivity() as? ToolbarController)?.setTitle("Партнёры и друзья")
    }
}
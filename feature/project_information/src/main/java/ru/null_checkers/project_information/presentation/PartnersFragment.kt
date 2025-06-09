package ru.null_checkers.project_information.presentation

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import ru.null_checkers.project_information.R
import ru.null_checkers.project_information.domain.use_case_impls.SetupLinkUseCase
import ru.null_checkers.ui.toolbar.ToolbarController

class PartnersFragment : Fragment(R.layout.fragment_partners) {

    private val setupLink = SetupLinkUseCase()

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

        setupLink(llSkanim, urlSkanim)
        setupLink(llIpfl, urlIpfl)
        setupLink(llWfolio, urlWfolio)
        setupLink(llMStore, urlMstore)
    }

    private fun setupTitle() {
        (requireActivity() as? ToolbarController)?.setTitle(
            getString(ru.null_checkers.ui.R.string.partnersFragmentTitle)
        )
    }
}
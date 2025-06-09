package ru.null_checkers.project_information.presentation

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import ru.null_checkers.project_information.R
import ru.null_checkers.project_information.domain.use_case_impls.SetupLinkUseCase
import ru.null_checkers.ui.toolbar.ToolbarController

class ContactsFragment : Fragment(R.layout.fragment_contacts) {

    private val setupLink = SetupLinkUseCase()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTitle()

        val llVk = view.findViewById<LinearLayout>(R.id.ll_vk)
        val llTelegram = view.findViewById<LinearLayout>(R.id.ll_tg)
        val llDzen = view.findViewById<LinearLayout>(R.id.ll_dz)

        val urlVk = "https://vk.com/serebro.gallery"
        val urlTelegram = "https://t.me/serebro_gallery"
        val urlDzen = "https://dzen.ru/serebro_gallery"

        setupLink(llVk, urlVk)
        setupLink(llTelegram, urlTelegram)
        setupLink(llDzen, urlDzen)
    }

    private fun setupTitle() {
        (requireActivity() as? ToolbarController)?.setTitle(
            getString(ru.null_checkers.ui.R.string.contactsFragmentTitle)
        )
    }
}
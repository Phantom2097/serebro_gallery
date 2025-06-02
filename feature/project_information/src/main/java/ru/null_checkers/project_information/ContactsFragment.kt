package ru.null_checkers.project_information

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import ru.null_checkers.common.use_cases.OpenUrlUseCase
import ru.null_checkers.ui.toolbar.ToolbarController

class ContactsFragment : Fragment(R.layout.fragment_contacts) {

    private val openUrl = OpenUrlUseCase()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTitle()

        val llVk = view.findViewById<LinearLayout>(R.id.ll_vk)
        val llTelegram = view.findViewById<LinearLayout>(R.id.ll_tg)
        val llDzen = view.findViewById<LinearLayout>(R.id.ll_dz)

        val urlVk = "https://vk.com/serebro.gallery"
        val urlTelegram = "https://t.me/serebro_gallery"
        val urlDzen = "https://dzen.ru/serebro_gallery"

        requireContext().apply {
            openUrl(this, llVk, urlVk)
            openUrl(this, llTelegram, urlTelegram)
            openUrl(this, llDzen, urlDzen)
        }
    }

    private fun setupTitle() {
        (requireActivity() as? ToolbarController)?.setTitle("Контакты")
    }
}
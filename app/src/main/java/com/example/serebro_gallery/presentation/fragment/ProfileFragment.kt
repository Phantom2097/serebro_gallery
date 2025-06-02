package com.example.serebro_gallery.presentation.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.serebro_gallery.R
import com.example.serebro_gallery.databinding.FragmentLinkBinding
import com.example.serebro_gallery.databinding.FragmentMainBinding
import com.example.serebro_gallery.databinding.FragmentProfileBinding
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.FragmentManager

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var tg: EditText
    private lateinit var tabs: TabLayout
    private lateinit var savebutton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameEditText = view.findViewById(R.id.name)
        surnameEditText = view.findViewById(R.id.surname)
        tg = view.findViewById(R.id.tg)
        savebutton = view.findViewById(R.id.savebutton)

        sharedPreferences = requireContext().getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)

        setupViewMode()
        setTabs()

        savebutton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val surname = surnameEditText.text.toString().trim()

            if (name.isBlank() || surname.isBlank()) {
                Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            sharedPreferences.edit()
                .putString("NAME", name)
                .putString("SURNAME", surname)
                .apply()

            Toast.makeText(requireContext(), "Данные сохранены", Toast.LENGTH_SHORT).show()

        }
    }
    private fun setupViewMode() {
        nameEditText.setText(sharedPreferences.getString("NAME", ""))
        surnameEditText.setText(sharedPreferences.getString("SURNAME", ""))

        nameEditText.hint = "Имя"
        surnameEditText.hint = "Фамилия"
        tg.hint = "Tg"
    }
    private fun setTabs(){
        println("!!! start")
        childFragmentManager.beginTransaction().replace(R.id.fragmentContainer, FavoriteFragment.newInstance()).commit()
        println("!!! replaced")
        tabs = view?.findViewById(R.id.tabs)!!
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> replaceFragment(FavoriteFragment.newInstance())
                    1 -> replaceFragment(GalleryFragment.newInstance())
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
package com.example.serebro_gallery.presentation.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.serebro_gallery.R

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var avatarImage: ImageView
    private lateinit var savebutton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameEditText = view.findViewById(R.id.name)
        surnameEditText = view.findViewById(R.id.surname)
        savebutton = view.findViewById(R.id.savebutton)
        avatarImage = view.findViewById(R.id.avatarImageView)

        sharedPreferences = requireContext().getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)

        setupViewMode()

        avatarImage.setOnClickListener {
            //openGallery
        }

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
    }
}
package com.example.serebro_gallery.presentation.fragment

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.edit
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.serebro_gallery.R
import com.example.serebro_gallery.presentation.viewmodel.ProfileViewModel
import com.google.android.material.tabs.TabLayout
import ru.null_checkers.form_filling_screen.ui.formfilling.MediaFile
import androidx.core.net.toUri

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText

    private lateinit var tg: EditText
    private lateinit var tabs: TabLayout

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
        tg = view.findViewById(R.id.tg)
        savebutton = view.findViewById(R.id.savebutton)
        avatarImage = view.findViewById(R.id.avatarImageView)

        sharedPreferences = requireContext().getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)

        setupViewMode()
        setTabs()

        avatarImage.setOnClickListener {
            openGallery()
        }

        savebutton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val surname = surnameEditText.text.toString().trim()

            if (name.isBlank() || surname.isBlank()) {
                Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            sharedPreferences.edit {
                putString("NAME", name)
                    .putString("SURNAME", surname)
            }

            Toast.makeText(requireContext(), "Данные сохранены", Toast.LENGTH_SHORT).show()

        }
    }

    private val viewModel by activityViewModels<ProfileViewModel>()
    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { imageUri ->
            sharedPreferences.edit {
                putString("AVATAR_URI", imageUri.toString())
            }

            loadImageWithGlide(imageUri)

            val fileName = DocumentFile.fromSingleUri(requireContext(), imageUri)?.name
                ?: uri.lastPathSegment.toString()
            viewModel.onItemClick(MediaFile(uri = imageUri, name = fileName))

        }
    }
    private fun openGallery() {
        galleryLauncher.launch(GALLERY_LAUNCHER_FILTER)
    }
    private companion object {
        private const val GALLERY_LAUNCHER_FILTER = "image/*"
    }

    private fun setupViewMode() {
        nameEditText.setText(sharedPreferences.getString("NAME", ""))
        surnameEditText.setText(sharedPreferences.getString("SURNAME", ""))

        nameEditText.hint = "Имя"
        surnameEditText.hint = "Фамилия"
        tg.hint = "Tg"

        sharedPreferences.getString("AVATAR_URI", null)?.let { uriString ->
            loadImageWithGlide(uriString.toUri())
        }
    }

    private fun loadImageWithGlide(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .circleCrop()
            .placeholder(R.drawable.logo_white)
            .error(R.drawable.logo_white)
            .into(avatarImage)
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
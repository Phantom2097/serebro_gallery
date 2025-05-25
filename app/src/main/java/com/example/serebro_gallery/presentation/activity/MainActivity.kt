package com.example.serebro_gallery.presentation.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.serebro_gallery.R
import com.example.serebro_gallery.databinding.ActivityMainBinding
import com.example.serebro_gallery.presentation.fragment.MainFragment
import com.example.serebro_gallery.presentation.viewmodel.MainViewModel
import kotlin.getValue

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtons()
    }

    private fun setupButtons() {
        binding.btnBackToMain.setOnClickListener {
            Log.d("NAV", "Back button clicked")
            navigateToMain()
        }

        binding.btnMenu.setOnClickListener {
            Log.d("NAV", "Menu button clicked")
            navigateToMenu()
        }
    }

    private fun navigateToMain() {
        try {
            viewModel.clearSelectedItem()
            val navController = findNavController(R.id.nav_host)

            navController.popBackStack(R.id.mainFragment, inclusive = false)
            navController.navigate(R.id.mainFragment)

        } catch (e: Exception) {
            Log.e("NAV", "Navigation error", e)
        }
    }

    private fun navigateToMenu() {
        try {
            viewModel.clearSelectedItem()
            val navController = findNavController(R.id.nav_host)

            navController.popBackStack(R.id.linkFragment, inclusive = false)
            navController.navigate(R.id.linkFragment)

        } catch (e: Exception) {
            Log.e("NAV", "Navigation error", e)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host).navigateUp() || super.onSupportNavigateUp()
    }
}
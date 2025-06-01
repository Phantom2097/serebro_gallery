package com.example.serebro_gallery.presentation.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.example.serebro_gallery.R
import com.example.serebro_gallery.databinding.ActivityMainBinding
import com.example.serebro_gallery.databinding.AppActionBarBinding
import com.example.serebro_gallery.presentation.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbarBinding: AppActionBarBinding

    val viewModel: MainViewModel by viewModels()
    private var lastFragmentId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        redefineOnBackPressed()

        setupToolBar()
    }


    private fun setupToolBar() {
        toolbarBinding = AppActionBarBinding.inflate(layoutInflater, binding.toolbar, false)

        binding.toolbar.addView(toolbarBinding.root)

        setupButtons()
    }

    // Возможно так нельзя, нужен какой-нибудь интерфейс или ещё что-то
    fun updateToolbarTitle(title: String) {
        toolbarBinding.toolbarTitle.text = title
    }

    private fun setupButtons() {
        toolbarBinding.btnBackToMain.setOnClickListener {
            Log.d("NAV", "Back button clicked")
            navigateToMain()
        }

        toolbarBinding.btnMenu.setOnClickListener {
            val navController = findNavController(R.id.nav_host)

            if (navController.currentDestination?.id == R.id.linkFragment) {
                if (lastFragmentId != null) {
                    navController.popBackStack()
                    navController.navigate(lastFragmentId!!)
                } else {
                    navController.popBackStack()
                }
            } else {
                navigateToMenu()
            }
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

            if (navController.currentDestination?.id != R.id.linkFragment) {
                lastFragmentId = navController.currentDestination?.id
            }

            navController.popBackStack(R.id.linkFragment, inclusive = false)
            navController.navigate(R.id.linkFragment)

        } catch (e: Exception) {
            Log.e("NAV", "Navigation error", e)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host).navigateUp() || super.onSupportNavigateUp()
    }

    /**
     * Переопределение нажатия кнопки назад
     *
     * На главном экране выходит из приложения, на других возвращает назад по графу навигации
     */
    private fun redefineOnBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController(R.id.nav_host).apply {
                    if (currentDestination?.label == "fragment_main") {
                        finish()
                    } else {
                        findNavController(R.id.nav_host).navigateUp()
                    }
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }
}
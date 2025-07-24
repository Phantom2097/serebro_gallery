package com.example.serebro_gallery.presentation.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.serebro_gallery.R
import com.example.serebro_gallery.presentation.shared_viewmodel.PhotoViewModel
import ru.null_checkers.common.entity.SharedViewModel
import ru.null_checkers.data.local.database.AppDatabase
import ru.null_checkers.data.local.repository.PhotoRepositoryImpl
import ru.null_checkers.exhibition.databinding.ActivityMainBinding
import ru.null_checkers.exhibition.presentation.exhibition_list.MainViewModel
import ru.null_checkers.ui.databinding.AppActionBarBinding
import ru.null_checkers.ui.toolbar.ToolbarController

class MainActivity : AppCompatActivity(), ToolbarController, SharedViewModel {
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

        setupUI()
    }

    private fun setupUI() {
        createToolbar()
        setupButtons()
    }

    private fun setupButtons() {
        toolbarBinding.btnBackToMain.setOnClickListener {
            Log.d("NAV", "Back button clicked")
            navigateToMain()
        }

        toolbarBinding.btnMenu.setOnClickListener {
            val navController = findNavController(ru.null_checkers.exhibition.R.id.nav_host)

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
            val navController = findNavController(ru.null_checkers.exhibition.R.id.nav_host)
            navController.navigate(ru.null_checkers.exhibition.R.id.exhibition_nav_graph)

        } catch (e: Exception) {
            Log.e("NAV", "Navigation error", e)
        }
    }

    private fun navigateToMenu() {
        try {
            viewModel.clearSelectedItem()

            val navController = findNavController(ru.null_checkers.exhibition.R.id.nav_host)

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
        return findNavController(ru.null_checkers.exhibition.R.id.nav_host).navigateUp() || super.onSupportNavigateUp()
    }

    /**
     * Переопределение нажатия кнопки назад
     *
     * На главном экране выходит из приложения, на других возвращает назад по графу навигации
     */
    private fun redefineOnBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController(ru.null_checkers.exhibition.R.id.nav_host).apply {
                    if (currentDestination?.label == "fragment_main") {
                        finish()
                    } else {
                        findNavController(ru.null_checkers.exhibition.R.id.nav_host).navigateUp()
                    }
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    private val database by lazy { AppDatabase.getDatabase(this) }
    private val repository by lazy { PhotoRepositoryImpl(database.photoDao()) }

    // Фабрика для ViewModel
    @Suppress("UNCHECKED_CAST")
    private val viewModelFactory by lazy {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PhotoViewModel(repository) as T
            }
        }
    }

    // Функция для доступа к ViewModel из фрагментов
    override fun getSharedPhotoViewModel(): PhotoViewModel {
        return ViewModelProvider(this, viewModelFactory)[PhotoViewModel::class.java]
    }

    override fun createToolbar() {
        toolbarBinding = AppActionBarBinding.inflate(layoutInflater, binding.toolbar, false)
        binding.toolbar.addView(toolbarBinding.root)
    }

    override fun setTitle(title: String) {
        toolbarBinding.toolbarTitle.text = title
    }
}
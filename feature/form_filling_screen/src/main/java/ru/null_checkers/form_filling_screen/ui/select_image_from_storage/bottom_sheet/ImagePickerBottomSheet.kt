package ru.null_checkers.form_filling_screen.ui.select_image_from_storage.bottom_sheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import ru.null_checkers.form_filling_screen.R
import ru.null_checkers.form_filling_screen.databinding.SelectImageFromStorageBinding
import ru.null_checkers.form_filling_screen.ui.formfilling.FormFillingViewModel
import ru.null_checkers.form_filling_screen.ui.select_image_from_storage.recycler.SelectFromStorageAdapter

/**
 * @author Phantom2097
 */
class ImagePickerBottomSheet : BottomSheetDialogFragment(R.layout.select_image_from_storage) {

    private var _binding: SelectImageFromStorageBinding? = null
    val binding get() = _binding!!

    private val viewModel by activityViewModels<FormFillingViewModel>()

    private val imagesAdapter by lazy {
        SelectFromStorageAdapter(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SelectImageFromStorageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initList()

        subscribeViewModel()

        setupBottomSheetBehavior()
    }

    override fun onStart() {
        super.onStart()

        dialog?.let {
            it.window?.setBackgroundDrawableResource(android.R.color.transparent)
            it.setCancelable(true)
        }
    }

    private fun initList() {
        val recyclerView = binding.selectFromStorageRecycler

        recyclerView.apply {
            adapter = imagesAdapter

            layoutManager = GridLayoutManager(context, SPAN_COUNT)
        }
    }

    /**
     * Функция обеспечивает наблюдение за изменениями данных во View Model
     */
    private fun subscribeViewModel() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    viewModel.mediaDataState.collect { state ->
                        Log.d("ImagePicker", "Received state with ${state.size} items: $state")
                        imagesAdapter.submitList(state)
                    }
                }
            }
        }
    }

    /**
     * Изменения оформления диалога
     */
    private fun setupBottomSheetBehavior() {
        val dialog = dialog as BottomSheetDialog
        val behavior = dialog.behavior
        behavior.isDraggable = true
        behavior.skipCollapsed = false
        behavior.isHideable = true
        Log.d("ImagePickerBottomSheet", "Behavior state: ${behavior.state}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private companion object {
        private const val SPAN_COUNT = 3
    }
}
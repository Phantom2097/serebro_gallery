package com.example.serebro_gallery.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.serebro_gallery.domain.models.ExhibitionItem

class MainViewModel : ViewModel() {
    private val _selectedItem = MutableLiveData<ExhibitionItem?>(null)
    val selectedItem: LiveData<ExhibitionItem?> = _selectedItem

    private val _currExhibition = MutableLiveData<ExhibitionItem?>(null)
    val currExhibition: LiveData<ExhibitionItem?> = _currExhibition

    fun selectItem(item: ExhibitionItem) {
        _selectedItem.value = item
    }
    fun clearSelectedItem() {
        _selectedItem.value = null
    }
    fun setExhibition(item: ExhibitionItem) {
        _currExhibition.value = item
    }

}
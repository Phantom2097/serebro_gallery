package com.example.serebro_gallery.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.serebro_gallery.data.RetrofitHelper
import com.example.serebro_gallery.domain.models.ExhibitionItem
import com.example.serebro_gallery.domain.models.PartnerItem
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class PartnersViewModel : ViewModel() {
    private val _partners = MutableLiveData<List<PartnerItem>>()
    val partners: LiveData<List<PartnerItem>> = _partners

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadPartners() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val htmlContent = RetrofitHelper.creatPartnersRetrofit().getPartnersPage()
                //println("!!! ${htmlContent}")
                //_partners.postValue(parseExhibitions(htmlContent))
            } catch (e: Exception) {
                println("!!! Error processing request: ${e.message}")
                e.printStackTrace()
            }
            _isLoading.value = false
        }
    }

}
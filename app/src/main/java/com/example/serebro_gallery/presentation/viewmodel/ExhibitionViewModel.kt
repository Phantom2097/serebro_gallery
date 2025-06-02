package com.example.serebro_gallery.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.serebro_gallery.data.RetrofitHelper
import com.example.serebro_gallery.domain.models.ExhibitionItem
import com.example.serebro_gallery.domain.models.PhotoItem
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class ExhibitionViewModel : ViewModel() {
    private val _photos = MutableLiveData<List<PhotoItem>>()
    val photos: LiveData<List<PhotoItem>> = _photos

    fun loadPhotos(pageUrl: String?) {
        viewModelScope.launch {
            try {
                val htmlContent = RetrofitHelper.creatPhotosRetrofit().getPhotosPage(pageUrl)
                _photos.postValue(parseExhibitions(htmlContent))
            } catch (e: Exception) {
                println("!!! Error processing request: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun parseExhibitions(htmlContent: String): List<PhotoItem> {
        val document = Jsoup.parse(htmlContent)
        val postList = document.select("section.js-gallery").first()

        return postList?.select("div.piece")?.mapNotNull { item ->
            try {
                val info = item.select("a.js-gallery-link")
                val name = info.attr("data-gallery-title").removePrefix("Автор: ")
                val link = info.attr("href")
                //println("!!! $name, $link")
                PhotoItem(name, link)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } ?: emptyList()
    }

}
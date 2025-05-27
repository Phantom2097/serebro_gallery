package com.example.serebro_gallery.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.serebro_gallery.data.RetrofitHelper
import com.example.serebro_gallery.domain.models.ExhibitionItem
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class MainViewModel : ViewModel() {
    private val _exhibitions = MutableLiveData<List<ExhibitionItem>>() //MutableLiveData<ExhibitionItem?>(null)
    val exhibitions: LiveData<List<ExhibitionItem>> = _exhibitions

    private val _currExhibition = MutableLiveData<ExhibitionItem?>(null)
    val currExhibition: LiveData<ExhibitionItem?> = _currExhibition

    fun selectItem(item: ExhibitionItem) {
        _currExhibition.value = item
    }
    fun clearSelectedItem() {
        _currExhibition.value = null
    }

    fun loadExhibitions() {
        viewModelScope.launch {
            try {
                val htmlContent = RetrofitHelper.creatRetrofit().getExhibitionsPage()
                _exhibitions.postValue(parseExhibitions(htmlContent))
            } catch (e: Exception) {
                println("!!! Error processing request: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun parseExhibitions(htmlContent: String): List<ExhibitionItem> {
        val document = Jsoup.parse(htmlContent)
        val postList = document.select("div.post-list.-rows.-announcement-list").first()
        //println("Найдено: $postList")

        return postList?.select("div.post-container.arrow-container")?.mapNotNull { item ->
            try {
                val info = item.select(".post-caption")
                val name = info.select(".post-title").text()
                //println("Найдено имя: $name")
                val date = info.select("[datetime]").text()
                //println("Найдена дата: $date")
                val description = info.select(".post-description").text()
                //println("Найдено описание: $description")

                val mainPhotoID = "https:" + item.select("img[data-src^='//i.wfolio.ru/x/']")
                    .attr("data-src")
                    .substringBefore("https:/")
                //println("Найдена ссылка: $mainPhotoID")
                ExhibitionItem(date, name, description, mainPhotoID)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } ?: emptyList()
    }

}
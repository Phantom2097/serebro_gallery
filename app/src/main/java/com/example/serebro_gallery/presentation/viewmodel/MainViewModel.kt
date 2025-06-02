package com.example.serebro_gallery.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serebro_gallery.data.RetrofitHelper
import com.example.serebro_gallery.domain.models.ExhibitionItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class MainViewModel : ViewModel() {
    private val _exhibitions = MutableLiveData<List<ExhibitionItem>>()
    val exhibitions: LiveData<List<ExhibitionItem>> = _exhibitions

    private val _state = MutableLiveData<ExhibitionsState>()
    val state: LiveData<ExhibitionsState> = _state

    private val _currExhibition = MutableLiveData<ExhibitionItem?>(null)
    val currExhibition: LiveData<ExhibitionItem?> = _currExhibition

    fun selectItem(item: ExhibitionItem) {
        _currExhibition.value = item
    }
    fun clearSelectedItem() {
        _currExhibition.value = null
    }

    fun loadExhibitions() {
        println("!!! загрузка выставок")
        _state.value = ExhibitionsState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(500)
                val htmlContent = RetrofitHelper.creatExhibitionRetrofit().getExhibitionsPage()
                _exhibitions.postValue(parseExhibitions(htmlContent))

                withContext(Dispatchers.Main) {
                    _state.value = exhibitions.value?.let { ExhibitionsState.Success(it) }
                }
            } catch (e: Exception) {
                println("!!! Error processing request: ${e.message}")
                e.printStackTrace()

                withContext(Dispatchers.Main) {
                    _state.value = ExhibitionsState.Error(
                        e.message ?: "Произошла неизвестная ошибка"
                    )
                }
            }
        }
    }

    private fun parseExhibitions(htmlContent: String): List<ExhibitionItem> {
        val document = Jsoup.parse(htmlContent)
        val postList = document.select("div.post-list.-rows.-announcement-list").first()
        //println("Найдено: $postList")

        return postList?.select("div.post-container.arrow-container")?.mapNotNull { item ->
            try {
                val link = item.select(".post-miniature").attr("href")
                //println("!!! $link")
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
                ExhibitionItem(date, name, description, mainPhotoID, link)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } ?: emptyList()
    }
}
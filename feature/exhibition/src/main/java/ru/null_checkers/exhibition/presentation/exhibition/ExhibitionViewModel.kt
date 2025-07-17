package ru.null_checkers.exhibition.presentation.exhibition

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import ru.null_checkers.data.remote.RetrofitHelper
import ru.null_checkers.exhibition.models.PhotoItem
import ru.null_checkers.exhibition.models.PrizePhoto

class ExhibitionViewModel : ViewModel() {
    private val _photos = MutableLiveData<List<PhotoItem>>()
    val photos: LiveData<List<PhotoItem>> = _photos

    private val _prizephoto = MutableLiveData<PrizePhoto>()
    val prizephoto: LiveData<PrizePhoto> = _prizephoto
    lateinit var exhibitionName: String

    fun loadPhotos(pageUrl: String?, exName: String) {
        viewModelScope.launch {
            exhibitionName = exName
            try {
                val htmlContent = RetrofitHelper.creatPhotosRetrofit().getPhotosPage(pageUrl)
                _photos.postValue(parseExhibitions(htmlContent))
                _prizephoto.postValue(parsePrizePhoto(htmlContent))
            } catch (e: Exception) {
                println("!!! Error processing request: ${e.message}")
                _prizephoto.postValue(PrizePhoto(null))
                e.printStackTrace()
            }
        }
    }

    private fun parsePrizePhoto(htmlContent: String): PrizePhoto {

        val authorIndex = htmlContent.indexOf("Автор")
        val substringBeforeAuthor = htmlContent.substring(0, authorIndex)
        val imgTagStart = substringBeforeAuthor.lastIndexOf("<img")
        val imgTagEnd = substringBeforeAuthor.indexOf(">", imgTagStart)
        val imgTag = substringBeforeAuthor.substring(imgTagStart, imgTagEnd + 1)
        val srcRegex = """(data-src|src)=["']([^"']+)["']""".toRegex()
        val matchResult = srcRegex.find(imgTag)

        val imageID = matchResult?.groupValues?.get(2)

        return PrizePhoto(imageID)
    }

    private fun parseExhibitions(htmlContent: String): List<PhotoItem> {
        val document = Jsoup.parse(htmlContent)
        val postList = document.select("section.js-gallery").first()

        return postList?.select("div.piece")?.mapNotNull { item ->
            try {
                val info = item.select("a.js-gallery-link")
                val name = info.attr("data-gallery-title").removePrefix("Автор: ")
                val link = info.attr("href")
                PhotoItem(name, link)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } ?: emptyList()
    }
}
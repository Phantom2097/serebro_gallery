package ru.null_checkers.exhibition.presentation.exhibition

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import ru.null_checkers.data.remote.RetrofitHelper
import ru.null_checkers.exhibition.models.PhotoItem
import ru.null_checkers.exhibition.models.PrizePhoto
import ru.null_checkers.exhibition.presentation.exhibition_list.state.AllPhotosState
import ru.null_checkers.exhibition.presentation.exhibition_list.state.PrizePhotoState
import java.time.LocalDate

class ExhibitionViewModel : ViewModel() {
    private val _statePrize = MutableLiveData<PrizePhotoState>()
    val statePrize: LiveData<PrizePhotoState> = _statePrize

    private val _stateAllPhotos = MutableLiveData<AllPhotosState>()
    val stateAllPhotos: LiveData<AllPhotosState> = _stateAllPhotos

    private val _photos = MutableLiveData<List<PhotoItem>>()
    val photos: LiveData<List<PhotoItem>> = _photos

    private val _prizephoto = MutableLiveData<PrizePhoto>()
    val prizephoto: LiveData<PrizePhoto> = _prizephoto
    lateinit var exhibitionName: String
    private val _prizephoto = MutableLiveData<PhotoItem>()
    val prizephoto: LiveData<PhotoItem> = _prizephoto

    private val _description = MutableLiveData<String?>()
    val description: LiveData<String?> = _description

    fun loadPhotos(pageUrl: String?, exName: String) {
    fun loadPhotos(pageUrl: String?) {
        _statePrize.value = PrizePhotoState.Loading
        _stateAllPhotos.value = AllPhotosState.Loading

        viewModelScope.launch {
            exhibitionName = exName
            // Загрузка HTML
            val htmlContent = try {
                RetrofitHelper.creatPhotosRetrofit().getPhotosPage(pageUrl)
            } catch (e: Exception) {
                println("!!! Error loading HTML: ${e.message}")
                withContext(Dispatchers.Main) {
                    _statePrize.value = PrizePhotoState.Error("Ошибка загрузки страницы")
                    _stateAllPhotos.value = AllPhotosState.Error("Ошибка загрузки страницы")
                }
                return@launch
            }

            // Обработка prizePhoto
            try {
                val prizePhoto = parsePrizePhoto(htmlContent)
                _prizephoto.postValue(prizePhoto)
                withContext(Dispatchers.Main) {
                    _statePrize.value = PrizePhotoState.Success(prizePhoto)
                }
            } catch (e: Exception) {
                println("!!! Error parsing prize photo: ${e.message}")
                withContext(Dispatchers.Main) {
                    _statePrize.value = PrizePhotoState.Error("Ошибка обработки первого места")
                }
            }

            // Обработка allPhotos
            try {
                val allPhotos = parseExhibitions(htmlContent)
                _photos.postValue(allPhotos)
                withContext(Dispatchers.Main) {
                    _stateAllPhotos.value = AllPhotosState.Success(allPhotos)
                }
            } catch (e: Exception) {
                println("!!! Error parsing all photos: ${e.message}")
                withContext(Dispatchers.Main) {
                    _stateAllPhotos.value = AllPhotosState.Error("Ошибка обработки фотографий")
                }
            }

            // Обработка описания (description)
            try {
                val description = findExhibitionAbout(htmlContent)
                _description.postValue(description)
                println("!!! $description")
            } catch (e: Exception) {
                println("!!! Error parsing description: ${e.message}")
                _description.postValue(null)
            }
        }
    }

    private fun findExhibitionAbout(html: String): String? {
        val doc = Jsoup.parse(html)

        // Ищем заголовок "Про выставку" (регистронезависимо)
        val aboutHeader = doc.select("h1, h2, h3, h4, h5, h6").first {
            it.text().equals("Про выставку", ignoreCase = true)
        } ?: return null

        // Ищем следующий за заголовком текстовый блок (например, <p>, <div> или просто текст)
        var nextElement: Element? = aboutHeader.nextElementSibling()
        while (nextElement != null) {
            if (nextElement.tagName() in listOf("p", "div", "section")) {
                return nextElement.text().takeIf { it.isNotBlank() }
            }
            nextElement = nextElement.nextElementSibling()
        }

        return null
    }

    private fun parsePrizePhoto(htmlContent: String): PhotoItem {
        return try {
            val doc = Jsoup.parse(htmlContent)

            val authorRow = doc.select("div.row").firstOrNull { row ->
                row.text().contains("Автор:") &&
                        row.select(".js-caption.wysiwyg:contains(Автор)").isEmpty()
            } ?: throw IllegalStateException("Не найден блок row с автором (исключая captions)")

            // 2. Извлекаем всю строку с "Автор:"
            val authorText = authorRow.select("p, div, span").firstOrNull { element ->
                element.text().contains("Автор:")
            }?.text()?.trim() ?: "Автор не указан"

            // 3. Вырезаем 2 слова после "Автор:"
            val authorName = authorText
                .substringAfter("Автор:") // Берем текст после "Автор:"
                .trim() // Убираем пробелы
                .split("\\s+".toRegex()) // Разбиваем по пробелам
                .take(2) // Берем первые 2 слова
                .joinToString(" ") // Соединяем обратно в строку
                .ifBlank { "Автор неизвестен" } // Fallback, если не нашли
            println("! $authorName")

            // 3. Ищем изображение в найденном row
            val img = authorRow.select("img").first()
                ?: throw IllegalStateException("В блоке не найдено изображений")

            // 4. Извлекаем URL из различных атрибутов
            val imageUrl = img.attr("data-src").ifBlank {
                throw IllegalStateException("Не найден data-src у изображения")
            }
            PhotoItem(imageUrl, authorName)
        } catch (e: Exception) {
            throw Exception("Ошибка парсинга: ${e.message}", e)
        }
    }

    private fun parseExhibitions(htmlContent: String): List<PhotoItem> {
        return try {
            val document = Jsoup.parse(htmlContent)
            val postList = document.select("section.js-gallery").first()
                ?: throw IllegalStateException("Section 'js-gallery' not found in HTML")

            postList.select("div.piece").map { item ->
                val info = item.select("a.js-gallery-link")
                val name = info.attr("data-gallery-title").removePrefix("Автор: ")
                    .takeIf { it.isNotBlank() }
                    ?: throw IllegalStateException("Empty or invalid author name")
                val link = info.attr("href")
                    .takeIf { it.isNotBlank() }
                    ?: throw IllegalStateException("Empty or invalid photo link")

                PhotoItem(link, name)
            }
        } catch (e: Exception) {
            throw Exception("Failed to parse exhibitions: ${e.message}", e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkDeadline(description: String?): Boolean {
        val hasDeadline = description?.contains("дедлайн", ignoreCase = true) ?: return false

        if (!hasDeadline) return false

        // Парсим дату дедлайна из описания
        val deadlineDate = parseDeadlineDate(description)

        // Если не удалось распарсить дату - считаем что дедлайн актуален
        deadlineDate ?: return true

        // Сравниваем с текущей датой
        return !deadlineDate.isBefore(LocalDate.now())
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseDeadlineDate(description: String): LocalDate? {
        return try {
            val pattern = """дедлайн\s*—\s*(\d{1,2})\s+([а-я]+)""".toRegex(RegexOption.IGNORE_CASE)

            val match = pattern.find(description) ?: return null

            val day = match.groupValues[1].toInt()
            val monthName = match.groupValues[2]

            // Конвертируем название месяца в номер
            val month = when (monthName.lowercase()) {
                "января" -> 1
                "февраля" -> 2
                "марта"-> 3
                "апреля" -> 4
                "мая"-> 5
                "июня"-> 6
                "июля"-> 7
                "августа" -> 8
                "сентября"-> 9
                "октября" -> 10
                "ноября" -> 11
                "декабря" -> 12
                else -> return null
            }

            // Создаем дату (текущий год + найденные день/месяц)
            LocalDate.of(LocalDate.now().year, month, day)
        } catch (e: Exception) {
            null
        }
    }
}
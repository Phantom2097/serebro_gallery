package com.example.serebro_gallery.presentation.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.serebro_gallery.R
import com.example.serebro_gallery.databinding.ActivityMainBinding
import com.example.serebro_gallery.databinding.FragmentAboutProjectBinding
import com.example.serebro_gallery.databinding.FragmentExhibitionBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jsoup.Jsoup
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

class AboutProjectFragment : Fragment() {

    private lateinit var webView: WebView
    private lateinit var binding: FragmentAboutProjectBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_about_project, container, false)
        webView = view.findViewById(R.id.webView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //// Настройка WebView
        //webView.settings.javaScriptEnabled = true
        //webView.settings.domStorageEnabled = true
        //webView.settings.mediaPlaybackRequiresUserGesture = false
//
        //// Загрузка видео
        //webView.loadUrl("https://vk.com/video-213066984_456239018?embed")

        binding = FragmentAboutProjectBinding.bind(view)
        binding.button2.setOnClickListener{
            println("!!! start")
            //extracted()
        }

    }

    override fun onDestroyView() {
        webView.destroy()
        super.onDestroyView()
    }
}
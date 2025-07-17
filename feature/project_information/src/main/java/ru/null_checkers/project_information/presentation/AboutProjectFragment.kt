package ru.null_checkers.project_information.presentation

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.VideoView
import androidx.fragment.app.Fragment
import com.facebook.shimmer.ShimmerFrameLayout
import ru.null_checkers.project_information.R
import ru.null_checkers.ui.toolbar.ToolbarController

class AboutProjectFragment : Fragment() {

    private lateinit var videoView: VideoView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_about_project, container, false)
        return view
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Если оставить в onCreateView, то фрагмент долго грузится
        videoView = view.findViewById(R.id.videoView)

        val shimmerView = view.findViewById<ShimmerFrameLayout>(R.id.shimmerView)

        setupTitle()

        shimmerView.startShimmer()

        try {
            val videoUri = Uri.parse("android.resource://${requireContext().packageName}/${R.raw.video_about}")

            videoView.setVideoURI(videoUri)
            videoView.setOnPreparedListener { mp ->
                mp.start() // Запуск только после готовности
                mp.isLooping = true // Опционально: зацикливание
                shimmerView.stopShimmer()
            }

            videoView.setOnErrorListener { _, what, extra ->
                Log.e("VIDEO", "Ошибка воспроизведения: $what/$extra")
                true
            }
        } catch (e: Exception) {
            Log.e("VIDEO", "Ошибка инициализации: ${e.message}")
        }
    }

    override fun onDestroyView() {
        //videoView.destroy()
        super.onDestroyView()
    }

    private fun AboutProjectFragment.setupTitle() {
        (requireActivity() as? ToolbarController)?.setTitle(
            getString(ru.null_checkers.ui.R.string.aboutProjectFragmentTitle)
        )
    }
}
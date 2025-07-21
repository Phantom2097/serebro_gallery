package ru.null_checkers.project_information.presentation

import android.annotation.SuppressLint
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import android.widget.VideoView
import androidx.fragment.app.Fragment
import com.facebook.shimmer.ShimmerFrameLayout
import ru.null_checkers.project_information.R
import ru.null_checkers.ui.toolbar.ToolbarController

class AboutProjectFragment : Fragment() {

    private lateinit var videoView: VideoView
    private lateinit var shimmerView: ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about_project, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoView = view.findViewById(R.id.videoView)
        shimmerView = view.findViewById(R.id.shimmerView)
        shimmerView.startShimmer()

        println("!! 1")

        setupTitle()
        setupVideoPlayer()
    }

    private fun setupVideoPlayer() {
        try {
            // 2. Формируем URI для видеоресурса
            val videoUri = Uri.parse("android.resource://${requireContext().packageName}/${R.raw.video_about}")
            Log.d("VIDEO_URI", "Loading video from: $videoUri")

            // 3. Настраиваем VideoView
            videoView.apply {

                // 4. Устанавливаем обработчики ДО начала загрузки
                setOnPreparedListener { mp ->
                    Log.d("VIDEO", "Video prepared - starting playback")
                    mp.isLooping = true // Зацикливание при необходимости
                    // Плавное переключение после загрузки
                    Handler(Looper.getMainLooper()).postDelayed({
                        shimmerView.stopShimmer()
                        shimmerView.visibility = View.GONE
                        mp.start()
                    }, 300) // Короткая задержка для плавности
                }

                setOnErrorListener { _, what, extra ->
                    Log.e("VIDEO_ERROR", "Playback failed. What: $what, Extra: $extra")
                    shimmerView.stopShimmer()
                    shimmerView.visibility = View.GONE
                    videoView.visibility = View.GONE // Гарантированно скрываем видео при ошибке
                    Toast.makeText(context, "Ошибка воспроизведения видео", Toast.LENGTH_LONG).show()
                    true // Возвращаем true, чтобы показать, что ошибка обработана
                }
                // 5. Начинаем загрузку видео
                setVideoURI(videoUri)

                setOnClickListener {
                    if (isPlaying) pause() else start()
                }
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("VIDEO", "Video file not found", e)
            shimmerView.stopShimmer()
            shimmerView.visibility = View.GONE
            Toast.makeText(context, "Видеофайл не найден", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Log.e("VIDEO", "Initialization error", e)
            shimmerView.stopShimmer()
            shimmerView.visibility = View.GONE
            Toast.makeText(context, "Ошибка загрузки видео", Toast.LENGTH_LONG).show()
        }
    }

    private fun AboutProjectFragment.setupTitle() {
        (requireActivity() as? ToolbarController)?.setTitle(
            getString(ru.null_checkers.ui.R.string.aboutProjectFragmentTitle)
        )
    }
}
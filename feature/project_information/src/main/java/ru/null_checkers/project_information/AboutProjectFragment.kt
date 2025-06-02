package ru.null_checkers.project_information

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.facebook.shimmer.ShimmerFrameLayout
import ru.null_checkers.ui.toolbar.ToolbarController

class AboutProjectFragment : Fragment() {

    private lateinit var webView: WebView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_about_project, container, false)
        webView = view.findViewById(R.id.webView)
        return view
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val webView = view.findViewById<WebView>(R.id.webView)
        val shimmerView = view.findViewById<ShimmerFrameLayout>(R.id.shimmerView)

        setupTitle()

        webView.settings.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                shimmerView.stopShimmer()
                shimmerView.visibility = View.GONE
                webView.visibility = View.VISIBLE
            }
        }

        shimmerView.startShimmer()
        val embedCode = """
             <iframe 
                src="https://vk.com/video_ext.php?oid=-213066984&id=456239018&hash=abc123&hd=2" 
                width="100%" 
                height="300" 
                frameborder="0" 
             ></iframe>
        """

        webView.loadDataWithBaseURL(
            null,
            "<html><body>$embedCode</body></html>",
            "text/html",
            "UTF-8",
            null
        )
    }

    override fun onDestroyView() {
        webView.destroy()
        super.onDestroyView()
    }
}

private fun AboutProjectFragment.setupTitle() {
    (requireActivity() as? ToolbarController)?.setTitle("О проекте")
}
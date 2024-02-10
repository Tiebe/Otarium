package nl.tiebe.otarium.androidApp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.wearable.Wearable
import nl.tiebe.otarium.ui.login.getActivity
import nl.tiebe.otarium.utils.ui.Android

class WearLoginActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val onBackDispatcher = onBackPressedDispatcher

        Android.context = this
        Android.window = window

        val url = intent.extras?.getByteArray("uri")!!.decodeToString()
        val node = intent.extras?.getString("node")!!

        setContent {
            AndroidView(factory = {
                val webViewClient: WebViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView,
                        webResourceRequest: WebResourceRequest
                    ): Boolean {
                        val checkUrl = webResourceRequest.url.toString()

                        if (checkUrl.startsWith("m6loapp://oauth2redirect")) {
                            println("sending....")
                            Wearable.getMessageClient(applicationContext).sendMessage(node, "/login_completed", checkUrl.toByteArray())
                            return true
                        }

                        return false
                    }

                    override fun shouldInterceptRequest(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): WebResourceResponse? {
                        if (request?.url.toString().contains("playconsolelogin")) {
                            Wearable.getMessageClient(applicationContext).sendMessage(node, "/login_bypass", null)
                        }
                        return super.shouldInterceptRequest(view, request)
                    }
                }

                val webView = WebView(it).apply {
                    settings.javaScriptEnabled = true
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    this.webViewClient = webViewClient
                    loadUrl(url)
                }

                onBackDispatcher.addCallback {
                    if (webView.canGoBack()) webView.goBack()
                    else getActivity(it)?.finishAfterTransition()
                }

                webView
            }, update = {
                it.loadUrl(url)
            }, modifier = Modifier.fillMaxSize())
        }

    }

}
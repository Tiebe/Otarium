package nl.tiebe.otarium.ui.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.arkivanov.essenty.backhandler.BackCallback
import nl.tiebe.otarium.Data

@SuppressLint("SetJavaScriptEnabled")
@Composable
internal actual fun LoginScreen(component: LoginComponent) {
    Text("sad")
    println("teesdfdsxcfdsfds")

    AndroidView(factory = {
        val webViewClient: WebViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                webResourceRequest: WebResourceRequest
            ): Boolean {
                return if (!component.checkUrl(webResourceRequest.url.toString())) {
                    view.loadUrl(component.loginUrl.url)
                    true
                } else {
                    false
                }
            }

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                if (request?.url.toString().contains("playconsolelogin")) {
                    Log.d("BrowserFragment", "Signing in: ${request?.url}")
                    Data.storeLoginBypass = true
                    component.navigateToHomeScreen()

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
            loadUrl(component.loginUrl.url)
        }

        component.addBackHandler(BackCallback {
            if (webView.canGoBack()) webView.goBack()
            else getActivity(it)?.finishAfterTransition()
        })

        webView
    }, update = {
        it.loadUrl(component.loginUrl.url)
    })
}


fun getActivity(context: Context?): Activity? {
    return if (context is ContextWrapper) {
         if (context is Activity) {
            context
        } else {
            getActivity(context.baseContext)
        }
    } else {
        null
    }
}
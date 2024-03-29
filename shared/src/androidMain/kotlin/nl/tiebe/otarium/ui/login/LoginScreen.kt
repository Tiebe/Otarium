package nl.tiebe.otarium.ui.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.essenty.backhandler.BackCallback
import nl.tiebe.otarium.logic.root.login.LoginComponent

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
internal actual fun LoginScreen(component: LoginComponent) {
    Scaffold(modifier = Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize().padding(it)) {
            if (component.loading.subscribeAsState().value) {
                LoadingScreen()
            } else {
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
                                component.bypassLogin()

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
        }
    }
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
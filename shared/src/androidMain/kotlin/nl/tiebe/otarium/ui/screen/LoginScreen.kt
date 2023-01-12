package nl.tiebe.otarium.ui.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.tiebe.otarium.bypassStore
import nl.tiebe.otarium.utils.getUrl
import nl.tiebe.otarium.utils.ui.CBackHandler

@SuppressLint("SetJavaScriptEnabled")
@Composable
internal actual fun LoginScreen(componentContext: ComponentContext, onLogin: (Pair<Boolean, Pair<String, String?>>) -> Unit)  {
    val loginUrl = getUrl()

    var webView: CustomWebViewClient? = null

    val component = remember {
        CBackHandler(componentContext) {
            if (webView != null && webView!!.webView.canGoBack()) {
                webView!!.webView.goBack()
            } else {
                if (getActivity(webView?.webView?.context) != null) {
                    getActivity(webView?.webView?.context)?.finishAfterTransition()
                }
            }
        }
    }

    AndroidView(factory = {
        WebView(it).apply {
            settings.javaScriptEnabled = true
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            val customWebViewClient = CustomWebViewClient(loginUrl.second, { component.enableBackCallback(false) }, this, onLogin)
            webViewClient = customWebViewClient
            webView = customWebViewClient
            loadUrl(loginUrl.first)
        }
    }, update = {
        it.loadUrl(loginUrl.first)
    })
}

class CustomWebViewClient(private var codeVerifier: String, private val disableBackHandler: () -> Unit, val webView: WebView, private val onLogin: (Pair<Boolean, Pair<String, String?>>) -> Unit) :
    WebViewClient() {

        override fun shouldOverrideUrlLoading(
            view: WebView,
            webResourceRequest: WebResourceRequest
        ): Boolean {
            if (webResourceRequest.url.toString().startsWith("m6loapp://oauth2redirect")) {
                Log.d("BrowserFragment", "Redirected to: ${webResourceRequest.url}")
                val uri = Uri.parse(webResourceRequest.url.toString().replace("#", "?"))
                uri.getQueryParameter("error")?.let {
                    Log.d("BrowserFragment", "Error: $it")
                    val loginUrl = getUrl()
                    codeVerifier = loginUrl.second
                    view.loadUrl(loginUrl.first)
                }

                uri.getQueryParameter("code")
                    ?.let { code ->
                        runBlocking {
                            launch {
                                disableBackHandler()
                                onLogin(false to (code to codeVerifier))


                                //todo
                                /*FirebaseMessaging.getInstance().token.addOnCompleteListener(
                                    OnCompleteListener { task ->
                                        if (!task.isSuccessful) {
                                            Log.w("Firebase", "Fetching FCM registration token failed", task.exception)
                                            return@OnCompleteListener
                                        }

                                        // Get new FCM registration token
                                        val token = task.result
                                        runBlocking {
                                            launch {
                                                sendFirebaseToken(
                                                    login.accessTokens.accessToken,
                                                    token
                                                )
                                            }
                                        }
                                    }
                                )*/


                            }
                        }
                    }

            } else {
                view.loadUrl(webResourceRequest.url.toString())
            }
            return true
        }

        override fun shouldInterceptRequest(
            view: WebView?,
            request: WebResourceRequest?
        ): WebResourceResponse? {
            if (request?.url.toString().contains("playconsolelogin")) {
                Log.d("BrowserFragment", "Signing in: ${request?.url}")
                disableBackHandler()
                bypassStore(true)
                onLogin(false to ("" to null))
            }
            return super.shouldInterceptRequest(view, request)
        }
}

fun getActivity(context: Context?): Activity? {
    if (context == null) {
        return null
    } else if (context is ContextWrapper) {
        return if (context is Activity) {
            context
        } else {
            getActivity(context.baseContext)
        }
    }
    return null
}
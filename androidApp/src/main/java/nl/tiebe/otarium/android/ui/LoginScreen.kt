package nl.tiebe.otarium.android.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.tiebe.otarium.utils.server.LoginRequest
import nl.tiebe.otarium.utils.server.exchangeUrl
import nl.tiebe.otarium.utils.server.getUrl
import nl.tiebe.otarium.utils.server.sendFirebaseToken


var refresh = MutableStateFlow(0)

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LoginScreen() {
    val loginUrl = getUrl()

    var webView: CustomWebViewClient? = null

    val backPressed = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView != null && webView!!.webView.canGoBack()) {
                    webView!!.webView.goBack()
                } else {
                    if (getActivity(webView?.webView?.context) != null) {
                        getActivity(webView?.webView?.context)?.finishAfterTransition() ?: println("Could not get context")
                    }
                }
            }
        }
    }

    LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher?.addCallback(backPressed)

    AndroidView(factory = {
        WebView(it).apply {
            settings.javaScriptEnabled = true
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            val customWebViewClient = CustomWebViewClient(loginUrl.second, backPressed, this)
            webViewClient = customWebViewClient
            webView = customWebViewClient
            loadUrl(loginUrl.first)
        }
    }, update = {
        it.loadUrl(loginUrl.first)
    })
}

class CustomWebViewClient(private var codeVerifier: String, private val backPressed: OnBackPressedCallback, val webView: WebView) :
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
                            backPressed.remove()
                            val login = exchangeUrl(LoginRequest(code, codeVerifier))
                            println("finished")
                            refresh.value++

                            //get firebase token
                            FirebaseMessaging.getInstance().token.addOnCompleteListener(
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
                            )


                        }
                    }
                }

        } else {
            view.loadUrl(webResourceRequest.url.toString())
        }
        return true
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
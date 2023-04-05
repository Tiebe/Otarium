package nl.tiebe.otarium.ui.login

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.text.input.KeyboardType.Companion.Uri
import com.arkivanov.decompose.ComponentContext
import dev.tiebe.magisterapi.api.account.LoginFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.tiebe.otarium.magister.MagisterLogin
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSURL
import platform.Foundation.NSURLComponents
import platform.Foundation.NSURLRequest
import platform.WebKit.*
import platform.darwin.NSObject

@Composable
internal actual fun LoginScreen(componentContext: ComponentContext, onLogin: (MagisterLogin) -> Unit) {
    var loginUrl = remember { LoginFlow.createAuthURL() }

    UIKitView(
        factory = {
            val config = WKWebViewConfiguration()

            val webView = object : WKWebView(frame = CGRectMake(0.0, 0.0, 0.0, 0.0), configuration = config) {}
            webView
        },
        update = { webView: WKWebView ->
            val coordinator = object : WKNavigationDelegateProtocol, NSObject() {
                override fun webView(
                    webView: WKWebView,
                    decidePolicyForNavigationAction: WKNavigationAction,
                    decisionHandler: (WKNavigationActionPolicy) -> Unit
                ) {
                    val request = decidePolicyForNavigationAction.request
                    println(request)
                    /*if (request.URL?.absoluteString != null && request.URL!!.absoluteString!!.startsWith("m6loapp://oauth2redirect")) {
                        println("Redirected to: ${request.URL!!.absoluteString}")
                        val uri = request.URL!!.absoluteString!!.replace("#", "?")
                        uri.getQueryParameter("error")?.let {
                            println("Error: $it")
                            loginUrl = LoginFlow.createAuthURL()

                            webView.loadRequest(NSURLRequest(uRL = NSURL(string = loginUrl.url)))
                        }
                        val code = uri.getQueryParameter("code")

                        if (code != null) {
                            println("Code: $code")
                            runBlocking {
                                launch {
                                    onLogin(MagisterLogin(code, loginUrl.codeVerifier))
                                }
                            }
                        }
                    }*/

                    if (decidePolicyForNavigationAction.navigationType == WKNavigationTypeLinkActivated) {
                        val url = decidePolicyForNavigationAction.request.URL
                        if (url != null) {
                            webView.loadRequest(NSURLRequest(uRL = url))
                        }
                    }
                    decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyAllow)
                }
            }

            webView.navigationDelegate = coordinator

            webView.loadRequest(NSURLRequest(uRL = NSURL(string = loginUrl.url)))
        },
        modifier = Modifier.fillMaxSize(),
        interactive = true
    )
}

fun String.getQueryParameter(name: String): String? {
    val url = NSURLComponents(string = this)
    val queryItems = url.queryItems
    return queryItems?.find { it == name }?.toString()
}
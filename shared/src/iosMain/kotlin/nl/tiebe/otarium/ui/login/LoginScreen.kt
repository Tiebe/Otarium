package nl.tiebe.otarium.ui.login

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.*
import platform.darwin.NSObject

@Composable
internal actual fun LoginScreen(component: LoginComponent) {
    UIKitView(
        factory = {
            val config = WKWebViewConfiguration()
            val preferences = WKPreferences()
            preferences.javaScriptEnabled = true
            preferences.javaScriptCanOpenWindowsAutomatically = true

            config.setURLSchemeHandler(object : NSObject(), WKURLSchemeHandlerProtocol {
                @Suppress("CONFLICTING_OVERLOADS")
                override fun webView(
                    webView: WKWebView,
                    startURLSchemeTask: WKURLSchemeTaskProtocol
                ) {
                    if (!component.checkUrl(startURLSchemeTask.request.URL?.absoluteString!!)) {
                        webView.loadRequest(NSURLRequest(uRL = NSURL(string = component.loginUrl.url)))
                    }
                }

                @Suppress("CONFLICTING_OVERLOADS")
                override fun webView(
                    webView: WKWebView,
                    stopURLSchemeTask: WKURLSchemeTaskProtocol
                ) {}

            }, forURLScheme = "m6loapp")

            config.preferences = preferences

            val webView = object : WKWebView(frame = CGRectMake(0.0, 0.0, 0.0, 0.0), configuration = config) {}
            webView
        },
        update = { webView: WKWebView ->
            webView.loadRequest(NSURLRequest(uRL = NSURL(string = component.loginUrl.url)))
        },
        modifier = Modifier.fillMaxSize(),
        interactive = true
    )
}
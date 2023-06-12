@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package dev.tiebe.otarium.ui.login

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.tiebe.otarium.logic.login.LoginComponent
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.*
import platform.darwin.NSObject

@Composable
internal actual fun LoginScreen(component: LoginComponent) {
    if (component.loading.subscribeAsState().value) {
        LoadingScreen()
    } else {
        UIKitView(
            factory = {
                val config = WKWebViewConfiguration()
                val preferences = WKPreferences()
                preferences.javaScriptEnabled = true
                preferences.javaScriptCanOpenWindowsAutomatically = true

                config.setURLSchemeHandler(object : NSObject(), WKURLSchemeHandlerProtocol {
                    override fun webView(
                        webView: WKWebView,
                        startURLSchemeTask: WKURLSchemeTaskProtocol
                    ) {
                        if (!component.checkUrl(startURLSchemeTask.request.URL?.absoluteString!!)) {
                            webView.loadRequest(NSURLRequest(uRL = NSURL(string = component.loginUrl.url)))
                        }
                    }

                    override fun webView(
                        webView: WKWebView,
                        stopURLSchemeTask: WKURLSchemeTaskProtocol
                    ) {
                    }
                }, forURLScheme = "m6loapp")

                config.preferences = preferences

                config.userContentController.addUserScript(
                    WKUserScript(
                        source = getScript(),
                        injectionTime = WKUserScriptInjectionTime.WKUserScriptInjectionTimeAtDocumentStart,
                        false
                    )
                )

                config.userContentController.addScriptMessageHandler(
                    object : WKScriptMessageHandlerProtocol, NSObject() {
                        override fun userContentController(
                            userContentController: WKUserContentController,
                            didReceiveScriptMessage: WKScriptMessage
                        ) {
                            try {
                                val dict = didReceiveScriptMessage.body as Map<String, Any>
                                val url = dict["responseURL"] as String

                                if (url.contains("appstorelogin"))
                                    component.bypassLogin()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                    },
                    "handler"
                )

                val webView = object : WKWebView(frame = CGRectMake(0.0, 0.0, 0.0, 0.0), configuration = config) {}

                webView.loadRequest(NSURLRequest(uRL = NSURL(string = component.loginUrl.url)))

                webView
            },
            update = { webView: WKWebView ->
            },
            modifier = Modifier.fillMaxSize(),
            interactive = true
        )
    }
}

fun getScript(): String {
    return """
        var open = XMLHttpRequest.prototype.open;
        XMLHttpRequest.prototype.open = function() {
            this.addEventListener("load", function() {
                var message = {"status" : this.status, "responseURL" : this.responseURL}
                webkit.messageHandlers.handler.postMessage(message);
            });
            open.apply(this, arguments);
        };
    """.trimIndent()
}
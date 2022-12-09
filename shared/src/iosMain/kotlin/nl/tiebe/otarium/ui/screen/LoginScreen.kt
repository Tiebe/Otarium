package nl.tiebe.otarium.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import kotlinx.cinterop.CValue
import kotlinx.cinterop.cValue
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import nl.tiebe.otarium.ui.UIKitApplier
import platform.CoreGraphics.CGRect
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration

@Composable
internal actual fun LoginScreen(onLogin: () -> Unit) {
    ComposeNode<WKWebView, UIKitApplier>(
        factory = {
            val config = WKWebViewConfiguration()
            val frame = cValue<CGRect>()
            frame.place(CGRectZero.ptr)

            object : WKWebView(frame = frame, configuration = config) {

            }.apply {

                val request = NSURLRequest(NSURL(string = "https://google.com"))

                this.loadRequest(request)
            }
        },
        update = {

        }
    )
}
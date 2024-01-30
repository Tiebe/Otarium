package nl.tiebe.otarium.ui.utils

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.convert
import platform.Foundation.NSAttributedString
import platform.Foundation.NSString
import platform.Foundation.NSUnicodeStringEncoding
import platform.Foundation.dataUsingEncoding
import platform.UIKit.NSAttributedStringDocumentType
import platform.UIKit.NSForegroundColorAttributeName
import platform.UIKit.NSHTMLTextDocumentType
import platform.UIKit.NSLineBreakByTruncatingTail
import platform.UIKit.UIColor
import platform.UIKit.UITextView
import platform.UIKit.create
import platform.darwin.NSUInteger

@Composable
actual fun HtmlView(html: String, textColor: Int, linkColor: Int, maxLines: Int) {
    val textColor = Color(textColor)
    val linkColor = Color(linkColor)

    @Suppress("CAST_NEVER_SUCCEEDS")
    UIKitView(
        factory = {
            val textView = UITextView()

            val htmlData = (html as NSString).dataUsingEncoding(NSUnicodeStringEncoding) ?: return@UIKitView textView

            textView.attributedText = NSAttributedString.create(data = htmlData, options = mapOf(
                NSAttributedStringDocumentType to NSHTMLTextDocumentType
            ), documentAttributes = null, error = null) ?: return@UIKitView textView

            textView.textColor = UIColor(
                red = textColor.red.toDouble(),
                green = textColor.green.toDouble(),
                blue = textColor.blue.toDouble(),
                alpha = textColor.alpha.toDouble()
            )

            textView.linkTextAttributes = mapOf(
                NSForegroundColorAttributeName to UIColor(
                    red = linkColor.red.toDouble(),
                    green = linkColor.green.toDouble(),
                    blue = linkColor.blue.toDouble(),
                    alpha = linkColor.alpha.toDouble()
                )
            )

            if (maxLines > 0) {
                textView.textContainer.maximumNumberOfLines = maxLines.toULong().convert<NSUInteger>()
                textView.textContainer.lineBreakMode = NSLineBreakByTruncatingTail
            }

            textView
        },
        update = { view ->
            view.text = html
        },
        background = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    )
}
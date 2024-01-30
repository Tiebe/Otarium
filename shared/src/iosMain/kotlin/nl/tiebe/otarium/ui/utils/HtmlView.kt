@file:Suppress("NAME_SHADOWING")

package nl.tiebe.otarium.ui.utils

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.convert
import platform.Foundation.NSAttributedString
import platform.Foundation.NSString
import platform.Foundation.NSUnicodeStringEncoding
import platform.Foundation.dataUsingEncoding
import platform.UIKit.*
import platform.darwin.NSUInteger

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
@Composable
actual fun HtmlView(html: String, textColor: Int, linkColor: Int, backgroundColor: Int, maxLines: Int) {
    val textColorReplacement = if (textColor == 0) {
        LocalTextStyle.current.color.takeOrElse { LocalContentColor.current }.toArgb()
    } else textColor

    val textColor = Color(textColorReplacement)
    val linkColor = Color(linkColor)
    val backgroundColor = Color(backgroundColor)

    @Suppress("CAST_NEVER_SUCCEEDS")
    UIKitView(
        factory = {
            val textColorUIKit = UIColor(red = textColor.red.toDouble(), green = textColor.green.toDouble(), blue = textColor.blue.toDouble(), alpha = textColor.alpha.toDouble())
            val linkColorUIKit = UIColor(red = linkColor.red.toDouble(), green = linkColor.green.toDouble(), blue = linkColor.blue.toDouble(), alpha = linkColor.alpha.toDouble())

            var textView: UIView = UILabel()
            val htmlData = (html as NSString).dataUsingEncoding(NSUnicodeStringEncoding) ?: return@UIKitView textView

            if (maxLines <= 1) {
                textView = UILabel()
                textView.textColor = textColorUIKit

                textView.attributedText = NSAttributedString.create(
                    data = htmlData, options = mapOf(
                        NSAttributedStringDocumentType to NSHTMLTextDocumentType
                    ), documentAttributes = null, error = null
                ) ?: return@UIKitView textView

            } else {
                textView = UITextView()
                textView.textColor = textColorUIKit

                textView.attributedText = NSAttributedString.create(
                    data = htmlData, options = mapOf(
                        NSAttributedStringDocumentType to NSHTMLTextDocumentType
                    ), documentAttributes = null, error = null
                ) ?: return@UIKitView textView


                textView.linkTextAttributes = mapOf(
                    NSForegroundColorAttributeName to linkColorUIKit
                )

                textView.textContainer.maximumNumberOfLines = maxLines.toULong().convert<NSUInteger>()
                textView.textContainer.lineBreakMode = NSLineBreakByTruncatingTail
            }

            textView
        },
        update = { view ->
            val htmlData = (html as NSString).dataUsingEncoding(NSUnicodeStringEncoding) ?: return@UIKitView
            val text = NSAttributedString.create(
                data = htmlData, options = mapOf(
                    NSAttributedStringDocumentType to NSHTMLTextDocumentType
                ), documentAttributes = null, error = null
            ) ?: return@UIKitView

            if (view is UILabel) view.attributedText = text
            else if (view is UITextView) view.attributedText = text
        },
        background = backgroundColor,
        modifier = Modifier.fillMaxSize()
    )
}
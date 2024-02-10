@file:Suppress("NAME_SHADOWING", "CAST_NEVER_SUCCEEDS")

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
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.convert
import platform.Foundation.*
import platform.UIKit.*
import platform.darwin.NSObject
import platform.darwin.NSUInteger

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
@Composable
actual fun HtmlView(html: String, textColor: Int, linkColor: Int, backgroundColor: Int, maxLines: Int, onClick: () -> Unit) {
    val textColorReplacement = if (textColor == 0) {
        LocalTextStyle.current.color.takeOrElse { LocalContentColor.current }.toArgb()
    } else textColor

    val textColor = Color(textColorReplacement)
    val linkColor = Color(linkColor)
    val backgroundColor = Color(backgroundColor)

    UIKitView(
        factory = {
            val textView = UITextView()

            textView.addGestureRecognizer(UITapGestureRecognizer(
                object : NSObject() {
                    @ObjCAction
                    fun invokeBlock() {
                        onClick()
                    }
                },
                NSSelectorFromString("invokeBlock")
            ))
            textView.editable = false
            textView.userInteractionEnabled = true

            textView.textContainer.maximumNumberOfLines = maxLines.toULong().convert<NSUInteger>()
            textView.textContainer.lineBreakMode = NSLineBreakByTruncatingTail
            textView
        },
        update = { textView ->
            val textColorUIKit = UIColor(red = textColor.red.toDouble(), green = textColor.green.toDouble(), blue = textColor.blue.toDouble(), alpha = textColor.alpha.toDouble())
            val linkColorUIKit = UIColor(red = linkColor.red.toDouble(), green = linkColor.green.toDouble(), blue = linkColor.blue.toDouble(), alpha = linkColor.alpha.toDouble())
            val backgroundColorUIKit = UIColor(red = backgroundColor.red.toDouble(), green = backgroundColor.green.toDouble(), blue = backgroundColor.blue.toDouble(), alpha = backgroundColor.alpha.toDouble())

            val htmlData = (html as NSString).dataUsingEncoding(NSUTF8StringEncoding) ?: return@UIKitView
            val text = NSAttributedString.create(
                data = htmlData, options = mapOf(
                    NSAttributedStringDocumentType to NSHTMLTextDocumentType
                ), documentAttributes = null, error = null
            ) ?: return@UIKitView

            textView.attributedText = text

            textView.backgroundColor = backgroundColorUIKit
            textView.textColor = textColorUIKit
            textView.linkTextAttributes = mapOf(
                NSForegroundColorAttributeName to linkColorUIKit
            )
        },
        background = backgroundColor,
        modifier = Modifier.fillMaxSize()
    )
}
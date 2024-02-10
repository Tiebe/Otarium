package nl.tiebe.otarium.ui.utils

import android.text.TextUtils
import android.text.util.Linkify
import android.widget.TextView
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat

@Composable
actual fun HtmlView(html: String, textColor: Int, linkColor: Int, backgroundColor: Int, maxLines: Int) {
    val color = if (textColor == -1) {
        LocalTextStyle.current.color.takeOrElse { LocalContentColor.current }.toArgb()
    } else textColor

    AndroidView(
        factory = { context ->
            TextView(context).apply {
                autoLinkMask = (Linkify.WEB_URLS + Linkify.EMAIL_ADDRESSES)
                linksClickable = true

                setTextColor(color)
                setLinkTextColor(linkColor)
                if (backgroundColor != -1) setBackgroundColor(backgroundColor)

                if (maxLines > 0) {
                    setMaxLines(maxLines)
                    ellipsize = TextUtils.TruncateAt.END
                }
            }
        },
        update = { view ->//TODO: fix flags to match magister
            view.text = HtmlCompat.fromHtml(html, 0)
        }
    )

}
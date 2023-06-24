package dev.tiebe.otarium.ui.utils

import android.text.TextUtils
import android.text.util.Linkify
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat

@Composable
actual fun HtmlView(html: String, textColor: Int, linkColor: Int, maxLines: Int) {
    AndroidView(
        factory = { context ->
            TextView(context).apply {
                autoLinkMask = (Linkify.WEB_URLS + Linkify.EMAIL_ADDRESSES)
                linksClickable = true

                setTextColor(textColor)
                setLinkTextColor(linkColor)

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
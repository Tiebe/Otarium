package nl.tiebe.otarium.utils

import android.content.Intent
import android.net.Uri
import nl.tiebe.otarium.utils.ui.Android

actual fun copyToClipboard(text: String) {
    val clipboard = Android.context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
    val clip = android.content.ClipData.newPlainText("Accounts", text)
    clipboard.setPrimaryClip(clip)
}

actual fun getClipboardText(): String {
    val clipboard = Android.context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
    val clip = clipboard.primaryClip
    return clip?.getItemAt(0)?.text.toString()
}

actual fun openUrl(url: String) {
    var checkedUrl = url

    if (!url.startsWith("http://") && !url.startsWith("https://")) {
        checkedUrl = "http://$url"
    }

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(checkedUrl))
    Android.context.startActivity(intent)
}
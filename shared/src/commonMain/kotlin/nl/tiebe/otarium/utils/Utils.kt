package nl.tiebe.otarium.utils

expect fun copyToClipboard(text: String)

expect fun getClipboardText(): String

expect fun openUrl(url: String)
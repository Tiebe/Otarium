package nl.tiebe.otarium.utils

import io.ktor.utils.io.*

actual fun copyToClipboard(text: String) {

}

actual fun getClipboardText(): String {
    return ""
}

actual fun openUrl(url: String) {

}

actual fun getDownloadFileLocation(id: String, fileName: String): ByteWriteChannel {
    TODO("Not yet implemented")
}

actual fun openFileFromCache(id: String, fileName: String) {
    TODO()
}

actual fun dynamicColorsPossible(): Boolean {
    return false
}
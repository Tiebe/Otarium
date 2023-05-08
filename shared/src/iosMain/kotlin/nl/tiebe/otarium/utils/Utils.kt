package nl.tiebe.otarium.utils

import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.UIKit.UIDocumentInteractionController

actual fun copyToClipboard(text: String) {

}

actual fun getClipboardText(): String {
    return ""
}

actual fun openUrl(url: String) {

}

actual fun writeFile(id: String, fileName: String, data: ByteArray) {
    memScoped {
        val nsData = NSData.create(bytes = allocArrayOf(data), length = data.size.toULong())

        val cacheDir = NSFileManager.defaultManager.URLForDirectory(
            NSCachesDirectory,
            NSUserDomainMask,
            null,
            false,
            null
        )!!

        val directory = cacheDir.path!! + "/$id"
        val file = "$directory/$fileName"

        NSFileManager.defaultManager.createDirectoryAtPath(directory, true, null, null)
        NSFileManager.defaultManager.createFileAtPath(file, nsData, null)
    }
}

actual fun openFileFromCache(id: String, fileName: String) {
    val cacheDir = NSFileManager.defaultManager.URLForDirectory(NSCachesDirectory, NSUserDomainMask, null, false, null)!!

    val directory = cacheDir.path!! + "/$id"
    val file = "$directory/$fileName"

    UIDocumentInteractionController().apply {
        URL = NSURL.fileURLWithPath(file)
        presentPreviewAnimated(true)
    }

}
package nl.tiebe.otarium.utils

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.FileProvider
import nl.tiebe.otarium.utils.ui.Android
import java.io.File

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

actual fun writeFile(id: String, fileName: String, data: ByteArray) {
    val directory = File(Android.context.cacheDir, id)
    if (!directory.exists()) {
        directory.mkdir()
    }

    val file = File(directory, fileName)
    file.writeBytes(data)
}

actual fun openFileFromCache(id: String, fileName: String) {
    val file = File(File(Android.context.cacheDir, id), fileName)
    val intent = Intent(Intent.ACTION_VIEW)

    val fileUri = FileProvider.getUriForFile(Android.context, Android.context.applicationContext.packageName, file)

    intent.setData(fileUri)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    Android.context.startActivity(Intent.createChooser(intent, fileName))
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
actual fun dynamicColorsPossible(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
}

actual fun convertImageByteArrayToBitmap(imageData: ByteArray): ImageBitmap {
    val image = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
    return image.asImageBitmap()
}

actual suspend fun awaitFrame() {
    kotlinx.coroutines.android.awaitFrame()
}
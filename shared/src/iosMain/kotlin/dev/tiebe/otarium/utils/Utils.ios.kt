package dev.tiebe.otarium.utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image

actual fun convertImageByteArrayToBitmap(imageData: ByteArray): ImageBitmap {
    return Image.makeFromEncoded(imageData).toComposeImageBitmap()
}
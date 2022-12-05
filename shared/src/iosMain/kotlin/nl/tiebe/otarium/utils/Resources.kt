package nl.tiebe.otarium.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.seiko.imageloader.rememberAsyncImagePainter
import dev.icerock.moko.resources.AssetResource
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import platform.Foundation.NSURL

actual fun getLocalizedString(string: StringResource): String {
    return StringDesc.Resource(string).localized()
}

//TODO: this


@Composable
actual fun getIcon(icon: AssetResource): Painter {
    rememberAsyncImagePainter(url = icon.url)
}
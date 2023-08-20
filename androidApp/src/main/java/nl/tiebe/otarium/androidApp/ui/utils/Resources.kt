package nl.tiebe.otarium.androidApp.ui.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.Window
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import dev.icerock.moko.resources.FileResource
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc

@SuppressLint("StaticFieldLeak")
object Android {
    lateinit var window: Window
    lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    lateinit var context: Context
}

fun getLocalizedString(string: StringResource): String {
    return StringDesc.Resource(string).toString(context = Android.context)
}

fun Float.format(decimals: Int): String {
    val df = java.text.DecimalFormat()
    df.isGroupingUsed = true
    df.maximumFractionDigits = decimals
    df.isDecimalSeparatorAlwaysShown = true
    val formatted = df.format(this).replace(".", ",")

    if (formatted.split(",")[1].length < decimals) {
        return formatted + "0".repeat(decimals - formatted.split(",")[1].length)
    }

    return formatted
}

@Composable
fun getText(file: FileResource): String {
    return file.readText(LocalContext.current)
}
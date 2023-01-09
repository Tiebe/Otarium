package nl.tiebe.otarium.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.Window
import androidx.activity.result.ActivityResultLauncher
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc

@SuppressLint("StaticFieldLeak")
object Android {
    lateinit var window: Window
    lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    lateinit var context: Context
}

actual fun getLocalizedString(string: StringResource): String {
    return StringDesc.Resource(string).toString(context = Android.context)
}

actual fun Float.format(decimals: Int): String {
    val df = java.text.DecimalFormat()
    df.isGroupingUsed = false
    df.maximumFractionDigits = 2
    df.isDecimalSeparatorAlwaysShown = false
    return df.format(this)
}
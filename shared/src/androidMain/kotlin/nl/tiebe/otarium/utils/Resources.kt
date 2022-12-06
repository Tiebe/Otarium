package nl.tiebe.otarium.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc

@SuppressLint("StaticFieldLeak")
object Android {
    lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    lateinit var context: Context
}

actual fun getLocalizedString(string: StringResource): String {
    return StringDesc.Resource(string).toString(context = Android.context)
}
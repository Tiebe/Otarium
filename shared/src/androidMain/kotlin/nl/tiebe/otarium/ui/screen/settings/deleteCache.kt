package nl.tiebe.otarium.ui.screen.settings

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import nl.tiebe.otarium.utils.Android

actual fun deleteCache() {
    Android.context.cacheDir.deleteRecursively()
    // restart android app
    val intent = Intent(Android.context, Android.context::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    startActivity(Android.context, intent, null)
    Runtime.getRuntime().exit(0)
}
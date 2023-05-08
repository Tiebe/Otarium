package nl.tiebe.otarium.androidApp

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import com.arkivanov.decompose.defaultComponentContext
import nl.tiebe.otarium.RootView
import nl.tiebe.otarium.utils.refreshGradesBackground
import nl.tiebe.otarium.utils.reloadTokensBackground
import nl.tiebe.otarium.utils.ui.Android
import java.io.File


class MainActivity : AppCompatActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Android.context = this
        Android.requestPermissionLauncher = requestPermissionLauncher
        Android.window = window

        // has to be set in code or in theme
        window.decorView.setBackgroundColor(Color.WHITE)
        window.statusBarColor = Color.parseColor("#0F86E4")

        reloadTokensBackground()
        refreshGradesBackground()

        val rootComponentContext = defaultComponentContext()

        val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        val darkMode = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

        val colorScheme =
            when {
                dynamicColor && darkMode -> {
                    dynamicDarkColorScheme(Android.context)
                }

                dynamicColor && !darkMode -> {
                    dynamicLightColorScheme(Android.context)
                }
                else -> null
            }

        setContent {
            RootView(rootComponentContext, colorScheme)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        trimCache(this)
    }

    private fun trimCache(context: Context) {
        try {
            val dir = context.cacheDir
            if (dir != null && dir.isDirectory) {
                deleteDir(dir)
            }
        } catch (e: Exception) {
            // TODO: handle exception
        }
    }

    private fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            if (children != null) {
                for (i in children.indices) {
                    val success = deleteDir(File(dir, children[i]))
                    if (!success) {
                        return false
                    }
                }
            }
        }

        // The directory is now empty so delete it
        return dir!!.delete()
    }
}


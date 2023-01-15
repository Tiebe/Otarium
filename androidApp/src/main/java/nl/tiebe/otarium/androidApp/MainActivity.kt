package nl.tiebe.otarium.androidApp

import android.graphics.Color
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.arkivanov.decompose.defaultComponentContext
import nl.tiebe.otarium.RootView
import nl.tiebe.otarium.utils.refreshGradesBackground
import nl.tiebe.otarium.utils.reloadTokensBackground
import nl.tiebe.otarium.utils.ui.Android

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

        setContent {
            RootView(rootComponentContext)
        }
    }
}

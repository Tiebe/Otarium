package nl.tiebe.otarium.androidApp

import android.graphics.Color
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import moe.tlaster.precompose.lifecycle.PreComposeActivity
import moe.tlaster.precompose.lifecycle.setContent
import nl.tiebe.otarium.RootView
import nl.tiebe.otarium.utils.Android

class MainActivity : PreComposeActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // has to be set in code or in theme
        window.decorView.setBackgroundColor(Color.WHITE)
        window.statusBarColor = Color.parseColor("#0F86E4")

        Android.context = this
        Android.requestPermissionLauncher = requestPermissionLauncher
        setContent {
            RootView()
        }
    }
}

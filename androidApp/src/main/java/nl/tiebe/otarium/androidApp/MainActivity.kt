package nl.tiebe.otarium.androidApp

import android.graphics.Color
import android.os.Bundle
import moe.tlaster.precompose.lifecycle.PreComposeActivity
import moe.tlaster.precompose.lifecycle.setContent
import nl.tiebe.otarium.RootView
import nl.tiebe.otarium.utils.Android

class MainActivity : PreComposeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // has to be set in code or in theme
        window.decorView.setBackgroundColor(Color.WHITE)
        window.statusBarColor = Color.parseColor("#cc7000")

        Android.context = this
        setContent {
            RootView()
        }
    }
}

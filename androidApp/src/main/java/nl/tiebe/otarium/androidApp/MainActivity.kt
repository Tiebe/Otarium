package nl.tiebe.otarium.androidApp

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.androidApp.ui.home.HomeScreen
import nl.tiebe.otarium.androidApp.ui.login.LoginScreen
import nl.tiebe.otarium.androidApp.ui.onboarding.OnboardingScreen
import nl.tiebe.otarium.androidApp.ui.theme.OtariumTheme
import nl.tiebe.otarium.androidApp.ui.utils.Android
import nl.tiebe.otarium.logic.default.DefaultRootComponent
import nl.tiebe.otarium.logic.root.RootComponent
import nl.tiebe.otarium.setup
import java.io.File


class MainActivity : AppCompatActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Android.window = window

        setupTokenBackgroundTask(this)
        setupGradesBackgroundTask(this)
        setupMessagesBackgroundTask(this)

        val rootComponentContext = defaultComponentContext()

        val darkMode = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = !darkMode
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContent {
            ProvideComponentContext(rootComponentContext) {
                setup()

                OtariumTheme {
                    val component = DefaultRootComponent(rootComponentContext)
                    val currentScreen by component.currentScreen.subscribeAsState()

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        when (val screen = currentScreen) {
                            is RootComponent.ChildScreen.HomeChild -> HomeScreen(screen.component)
                            is RootComponent.ChildScreen.LoginChild -> LoginScreen(screen.component)
                            is RootComponent.ChildScreen.OnboardingChild -> OnboardingScreen(screen.component)
                        }
                    }
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val darkMode = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = !darkMode
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

    private val LocalComponentContext: ProvidableCompositionLocal<ComponentContext> =
        staticCompositionLocalOf { error("Root component context was not provided") }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {


        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @Composable
    internal fun ProvideComponentContext(componentContext: ComponentContext, content: @Composable () -> Unit) {
        CompositionLocalProvider(LocalComponentContext provides componentContext, content = content)
    }
}


package nl.tiebe.otarium.androidApp

import android.graphics.Color
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.arkivanov.decompose.defaultComponentContext
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.tiebe.otarium.RootView
import nl.tiebe.otarium.magister.Tokens
import nl.tiebe.otarium.utils.ui.Android
import nl.tiebe.otarium.utils.server.sendFirebaseToken

class MainActivity : AppCompatActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Android.context = this
        Android.requestPermissionLauncher = requestPermissionLauncher
        Android.window = window

        FirebaseApp.initializeApp(this)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("Firebase", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d("Firebase", token)

            Thread {
                runBlocking {
                    launch {
                        try {
                            sendFirebaseToken(
                                Tokens.getPastTokens()?.accessTokens?.accessToken ?: return@launch,
                                token
                            )
                        } catch (e: Exception) {
                            Looper.prepare()
                            Toast.makeText(
                                Android.context,
                                "Failed to connect to the server...",
                                Toast.LENGTH_SHORT
                            ).show()

                            Log.d("Firebase", e.toString())
                        }
                    }
                }
            }.start()
        })

        // has to be set in code or in theme
        window.decorView.setBackgroundColor(Color.WHITE)
        window.statusBarColor = Color.parseColor("#0F86E4")

        val rootComponentContext = defaultComponentContext()

        setContent {
            RootView(rootComponentContext)
        }
    }
}

package nl.tiebe.otarium.android

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.tiebe.otarium.Main
import nl.tiebe.otarium.android.ui.theme.OtariumTheme
import nl.tiebe.otarium.magister.Tokens
import nl.tiebe.otarium.utils.Android
import nl.tiebe.otarium.utils.server.sendFirebaseToken

class MainActivity : AppCompatActivity() {

    // yes this needs to be on top, fuck you android
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Android.context = this
        Android.requestPermissionLauncher = requestPermissionLauncher

        val main = Main()
        main.setup()

        setContent {
            OtariumTheme(dynamicColor = false) {
                main.start()
            }
        }

        createNotificationChannel(this)

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
                                this@MainActivity,
                                "Failed to connect to the server...",
                                Toast.LENGTH_SHORT
                            ).show()

                            Log.d("Firebase", e.toString())
                        }
                    }
                }
            }.start()
        })
    }

    private fun createNotificationChannel(context: MainActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.grades_channel)
            val descriptionText = context.getString(R.string.grades_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("grades", name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        println("INTENT")
    }

}
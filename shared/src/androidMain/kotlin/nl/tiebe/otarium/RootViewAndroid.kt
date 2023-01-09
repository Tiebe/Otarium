package nl.tiebe.otarium

import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.DefaultComponentContext
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.tiebe.otarium.magister.Tokens
import nl.tiebe.otarium.ui.navigation.ProvideComponentContext
import nl.tiebe.otarium.utils.Android
import nl.tiebe.otarium.utils.server.sendFirebaseToken

@Composable
fun RootView(rootComponentContext: DefaultComponentContext) {
    FirebaseApp.initializeApp(Android.context)

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

    ProvideComponentContext(rootComponentContext) {
        Content()
    }
}

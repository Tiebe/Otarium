package nl.tiebe.otarium.android

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.tiebe.otarium.Main
import nl.tiebe.otarium.android.ui.*
import nl.tiebe.otarium.android.ui.screen.LoginScreen
import nl.tiebe.otarium.android.ui.theme.OtariumTheme
import nl.tiebe.otarium.finishOnboarding
import nl.tiebe.otarium.isFinishedOnboarding
import nl.tiebe.otarium.magister.Tokens
import nl.tiebe.otarium.storeBypass
import nl.tiebe.otarium.utils.server.sendFirebaseToken

class MainActivity : AppCompatActivity() {

    // yes this needs to be on top, fuck you android
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val main = Main()
        main.setup()

        setContent {
            OtariumTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val openLoginScreen = remember { mutableStateOf(false) }
                    if (openLoginScreen.value) MainActivityScreen()

                    if (!isFinishedOnboarding()) {
                        OnBoarding(onFinish = {
                            openLoginScreen.value = true
                            finishOnboarding()
                        }, notifications = { askNotificationPermission(this)})
                    } else MainActivityScreen()
                }

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

            runBlocking {
                launch {
                    try {
                        sendFirebaseToken(
                            Tokens.getPastTokens()?.accessTokens?.accessToken ?: return@launch,
                            token
                        )
                    } catch (e: Exception) {
                        Tokens.clearTokens()
                        Toast.makeText(this@MainActivity, "Failed to connect to the server. Trying again...", Toast.LENGTH_LONG)
                            .show()

                        val intent = Intent(this@MainActivity, MainActivity::class.java)
                        startActivity(intent)
                        finishAffinity()

                        Log.d("Firebase", e.toString())


                    }
                }
            }
        })

        MobileAds.initialize(this) {

        }

    }


    @Composable
    fun MainActivityScreen() {
        if (storeBypass()) { Navigation(); return }
        val openMainScreen = remember { mutableStateOf(false) }

        if (openMainScreen.value) Navigation()

        if (Tokens.getPastTokens() == null) {
            LoginScreen(onLogin = {
                openMainScreen.value = true
            })
        } else {
            Navigation()
        }
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

    private fun askNotificationPermission(context: MainActivity) {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
                /*} else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                    // TODO: display an educational UI explaining to the user the features that will be enabled
                    //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                    //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                    //       If the user selects "No thanks," allow the user to continue without notifications.*/
            } else {
                // Directly ask for the permission
                context.requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }


}
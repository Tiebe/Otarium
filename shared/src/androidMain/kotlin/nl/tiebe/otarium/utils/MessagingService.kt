package nl.tiebe.otarium.utils

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.util.Log
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.runBlocking
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.magister.Tokens
import nl.tiebe.otarium.ui.theme.Blue80
import nl.tiebe.otarium.utils.server.sendFirebaseToken

class MessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.d("Firebase", "Refreshed token: $token")
        runBlocking {
            sendFirebaseToken(Tokens.getPastTokens()?.accessTokens?.accessToken ?: return@runBlocking, token)
        }
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    @Suppress("DEPRECATION")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("Firebase", "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("Firebase", "Message data payload: ${remoteMessage.data}")

        }

        remoteMessage.notification?.let {
            Log.d("Firebase", "Message Notification Body: ${it.body}")

            val intent = packageManager.getLaunchIntentForPackage(packageName)
            intent?.action = ".MainActivity"
            intent?.flags = 0
            val builder = NotificationCompat.Builder(this, "grades")
                .setSmallIcon(MR.images.ic_launcher.drawableResId)
                .setContentTitle(remoteMessage.notification!!.title)
                .setContentText(remoteMessage.notification!!.body)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, 0, intent, 0))
                .setGroup(System.currentTimeMillis().toString())
                .setColor(Blue80.toArgb())

            with(NotificationManagerCompat.from(this)) {
                notify(System.currentTimeMillis().toInt(), builder.build())
            }
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
}


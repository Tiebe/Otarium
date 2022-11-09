package nl.tiebe.otarium.android.utils

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.runBlocking
import nl.tiebe.otarium.android.R
import nl.tiebe.otarium.magister.Tokens
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
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("Firebase", "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("Firebase", "Message data payload: ${remoteMessage.data}")

        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("Firebase", "Message Notification Body: ${it.body}")

            val intent = packageManager.getLaunchIntentForPackage(packageName)
            intent?.action = ".MainActivity"
            intent?.flags = 0
            val builder = NotificationCompat.Builder(this, "grades")
                .setSmallIcon(R.drawable.ic_notification_foreground)
                .setContentTitle(remoteMessage.notification!!.title)
                .setContentText(remoteMessage.notification!!.body)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, 0, intent, 0))
                .setGroup(System.currentTimeMillis().toString())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                builder.color = resources.getColor(R.color.main_color, null)
            } else {
                builder.color = resources.getColor(R.color.main_color) // deprecated but required for older versions
            }

            with(NotificationManagerCompat.from(this)) {
                notify(System.currentTimeMillis().toInt(), builder.build())
            }
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
}


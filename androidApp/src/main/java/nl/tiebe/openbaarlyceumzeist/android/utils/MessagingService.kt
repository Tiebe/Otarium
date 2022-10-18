package nl.tiebe.openbaarlyceumzeist.android.utils

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.runBlocking
import nl.tiebe.openbaarlyceumzeist.magister.Tokens
import nl.tiebe.openbaarlyceumzeist.utils.server.sendFirebaseToken

class MessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.d("Firebase", "Refreshed token: $token")
        runBlocking {
            sendFirebaseToken(Tokens.getPastTokens()?.accessTokens?.accessToken ?: return@runBlocking, token)
        }
    }


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
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }


}
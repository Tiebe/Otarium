package nl.tiebe.otarium.androidApp

import android.content.Intent
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService


class WearListener : WearableListenerService() {
    override fun onMessageReceived(messageEvent: MessageEvent) {
        val intent = Intent(applicationContext, WearLoginActivity::class.java)
        intent.putExtra("uri", messageEvent.data)
        intent.putExtra("node", messageEvent.sourceNodeId)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        startActivity(intent)
    }

}
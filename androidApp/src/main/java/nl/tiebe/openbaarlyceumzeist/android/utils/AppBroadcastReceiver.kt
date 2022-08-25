package nl.tiebe.openbaarlyceumzeist.android.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AppBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.BOOT_COMPLETED" || intent?.action == "android.intent.action.MY_PACKAGE_REPLACED") {
            Background().updatePeriodically(context ?: return)
        }
    }
}
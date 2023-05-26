package nl.tiebe.otarium.androidApp

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import nl.tiebe.otarium.utils.setupGradesBackgroundTask
import nl.tiebe.otarium.utils.setupTokenBackgroundTask


class BootService : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return

        setupTokenBackgroundTask(context)
        setupGradesBackgroundTask(context)
    }
}

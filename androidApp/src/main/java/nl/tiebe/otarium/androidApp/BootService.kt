package nl.tiebe.otarium.androidApp

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import nl.tiebe.otarium.utils.refreshGradesBackground
import nl.tiebe.otarium.utils.reloadTokensBackground
import nl.tiebe.otarium.utils.ui.Android


class BootService : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        Android.context = context!!

        reloadTokensBackground()
        refreshGradesBackground()
    }
}

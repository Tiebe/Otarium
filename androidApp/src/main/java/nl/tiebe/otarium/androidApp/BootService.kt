package nl.tiebe.otarium.androidApp

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import nl.tiebe.otarium.utils.refreshGradesBackground
import nl.tiebe.otarium.utils.reloadTokensBackground


class BootService : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        reloadTokensBackground()
        refreshGradesBackground()
    }
}

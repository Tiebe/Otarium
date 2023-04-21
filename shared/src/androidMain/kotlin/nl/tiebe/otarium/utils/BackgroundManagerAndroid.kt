package nl.tiebe.otarium.utils

import android.Manifest
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.runBlocking
import nl.tiebe.otarium.R
import nl.tiebe.otarium.magister.refreshGrades
import nl.tiebe.otarium.ui.theme.Blue80
import nl.tiebe.otarium.utils.ui.Android
import java.util.concurrent.TimeUnit

actual fun reloadTokensBackground(delay: Long) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val backgroundRequest = PeriodicWorkRequest.Builder(TokenRefreshWorker::class.java, 45, TimeUnit.MINUTES).setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(true)
                    .build()
            ).setInitialDelay(delay, TimeUnit.SECONDS).build()

        WorkManager.getInstance(Android.context).enqueueUniquePeriodicWork("tokens", ExistingPeriodicWorkPolicy.REPLACE, backgroundRequest)
    } else {
        //TODO: android version below oreo
    }
}

actual fun refreshGradesBackground(delay: Long) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val backgroundRequest = PeriodicWorkRequest.Builder(GradeRefreshWorker::class.java, 15, TimeUnit.MINUTES).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()
        ).setInitialDelay(delay, TimeUnit.SECONDS).build()

        WorkManager.getInstance(Android.context).enqueueUniquePeriodicWork("grades", ExistingPeriodicWorkPolicy.REPLACE, backgroundRequest)
    } else {
        //TODO: android version below oreo
    }
}

class TokenRefreshWorker(appContext: Context, workerParams: WorkerParameters): ListenableWorker(appContext, workerParams) {
    override fun startWork(): ListenableFuture<Result> {
        val outputData = Data.Builder()

        runBlocking {
            try {
                nl.tiebe.otarium.Data.accounts.forEach {
                    it.refreshTokens()
                }
                outputData.putBoolean("success", true)
            } catch (e: Exception) {
                outputData.putString("error", e.toString())
                e.stackTrace.forEachIndexed { index, stackTraceElement -> outputData.putString("error$index", stackTraceElement.toString()) }
                e.printStackTrace()
            }
        }

        return Futures.immediateFuture(Result.success(outputData.build()))
    }
}

class GradeRefreshWorker(appContext: Context, workerParams: WorkerParameters): ListenableWorker(appContext, workerParams) {
    override fun startWork(): ListenableFuture<Result> {
        runBlocking {
            nl.tiebe.otarium.Data.selectedAccount.refreshGrades()
        }

        return Futures.immediateFuture(Result.success())
    }
}

actual fun sendNotification(title: String, message: String) {
    val intent = Android.context.packageManager.getLaunchIntentForPackage(Android.context.packageName)
    intent?.action = ".MainActivity"
    intent?.flags = 0
    val builder = NotificationCompat.Builder(Android.context, "grades")
        .setSmallIcon(R.drawable.ic_notification_foreground)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(1)
        .setAutoCancel(true)
        .setContentIntent(PendingIntent.getActivity(Android.context, 0, intent, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) FLAG_MUTABLE else FLAG_CANCEL_CURRENT))
        .setGroup(System.currentTimeMillis().toString())
        .setColor(Blue80.toArgb())

    with(NotificationManagerCompat.from(Android.context)) {
        if (ActivityCompat.checkSelfPermission(
                Android.context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notify(System.currentTimeMillis().toInt(), builder.build())
    }

}
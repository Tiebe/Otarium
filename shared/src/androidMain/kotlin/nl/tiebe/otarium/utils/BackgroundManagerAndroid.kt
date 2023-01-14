package nl.tiebe.otarium.utils

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.Context
import android.os.Build
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.ui.theme.Blue80
import nl.tiebe.otarium.utils.ui.Android
import java.util.concurrent.TimeUnit

actual fun reloadTokensBackground() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val backgroundRequest = PeriodicWorkRequest.Builder(TokenRefreshWorker::class.java, 45, TimeUnit.MINUTES).setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(true)
                    .build()
            ).build()

        WorkManager.getInstance(Android.context).enqueueUniquePeriodicWork("tokens", ExistingPeriodicWorkPolicy.REPLACE, backgroundRequest)
    } else {
        //TODO: android version below oreo
    }
}

actual fun refreshGradesBackground() {
    val startTime = Clock.System.now()
    val dateTime = startTime.toLocalDateTime(TimeZone.UTC)
    val minutes = dateTime.minute % 15

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val backgroundRequest = PeriodicWorkRequest.Builder(GradeRefreshWorker::class.java, 15, TimeUnit.MINUTES).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()
        ).setInitialDelay((15 - minutes).toLong(), TimeUnit.MINUTES).build()

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
                refreshTokens()
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
            refreshGrades()
        }

        return Futures.immediateFuture(Result.success())
    }
}

actual fun sendNotification(title: String, message: String) {
    val intent = Android.context.packageManager.getLaunchIntentForPackage(Android.context.packageName)
    intent?.action = ".MainActivity"
    intent?.flags = 0
    val builder = NotificationCompat.Builder(Android.context, "grades")
        .setSmallIcon(MR.images.ic_launcher.drawableResId)
        .setContentTitle(title)
        .setContentText(message)
        .setAutoCancel(true)
        .setContentIntent(PendingIntent.getActivity(Android.context, 0, intent, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) FLAG_MUTABLE else FLAG_CANCEL_CURRENT))
        .setGroup(System.currentTimeMillis().toString())
        .setColor(Blue80.toArgb())

    with(NotificationManagerCompat.from(Android.context)) {
        notify(System.currentTimeMillis().toInt(), builder.build())
    }

}
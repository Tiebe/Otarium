package nl.tiebe.otarium.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.ui.graphics.Color
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
import nl.tiebe.otarium.magister.refreshMessages
import nl.tiebe.otarium.setup
import nl.tiebe.otarium.utils.ui.Android
import java.util.concurrent.TimeUnit

actual fun reloadTokensBackground(delay: Long) {
    setupTokenBackgroundTask(Android.context, delay)
}

fun setupTokenBackgroundTask(context: Context, delay: Long = 0) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val backgroundRequest = PeriodicWorkRequest.Builder(TokenRefreshWorker::class.java, 45, TimeUnit.MINUTES).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()
        ).setInitialDelay(delay, TimeUnit.SECONDS).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork("tokens", ExistingPeriodicWorkPolicy.UPDATE, backgroundRequest)
    } else {
        //TODO: android version below oreo
    }
}

actual fun refreshGradesBackground(delay: Long){
    setupGradesBackgroundTask(Android.context, delay)
}

fun setupGradesBackgroundTask(context: Context, delay: Long = 0) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val backgroundRequest = PeriodicWorkRequest.Builder(GradeRefreshWorker::class.java, 15, TimeUnit.MINUTES).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()
        ).setInitialDelay(delay, TimeUnit.SECONDS).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork("grades", ExistingPeriodicWorkPolicy.UPDATE, backgroundRequest)
    } else {
        //TODO: android version below oreo
    }
}

actual fun refreshMessagesBackground(delay: Long) {
    setupMessagesBackgroundTask(Android.context, delay)
}

fun setupMessagesBackgroundTask(context: Context, delay: Long = 0) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val backgroundRequest = PeriodicWorkRequest.Builder(MessageRefreshWorker::class.java, 15, TimeUnit.MINUTES).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()
        ).setInitialDelay(delay, TimeUnit.SECONDS).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork("messages", ExistingPeriodicWorkPolicy.UPDATE, backgroundRequest)
    } else {
        //todo: android version below oreo
    }
}

class TokenRefreshWorker(appContext: Context, workerParams: WorkerParameters): ListenableWorker(appContext, workerParams) {
    override fun startWork(): ListenableFuture<Result> {
        //sendDebugNotification(applicationContext, "Refreshing tokens", "Refreshing your tokens")

        setup()

        val outputData = Data.Builder()

        runBlocking {
            try {
                nl.tiebe.otarium.Data.accounts.forEach {
                    it.refreshTokens()
                }

                sendDebugNotification(applicationContext, "Tokens refreshed", "Your tokens have been refreshed")
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
    @SuppressLint("RestrictedApi")
    override fun startWork(): ListenableFuture<Result> {
        val data = Data.Builder()
        //sendDebugNotification(applicationContext, "Refreshing grades", "Refreshing your grades")

        setup()

        runBlocking {
            nl.tiebe.otarium.Data.selectedAccount.refreshGrades { title, message ->
                sendNotificationAndroid(
                    applicationContext,
                    title,
                    message
                )
            }

            sendDebugNotification(applicationContext, "Grades refreshed", "Your grades have been refreshed")
        }

        return Futures.immediateFuture(Result.success(data.build()))
    }
}

class MessageRefreshWorker(appContext: Context, workerParams: WorkerParameters): ListenableWorker(appContext, workerParams) {
    @SuppressLint("RestrictedApi")
    override fun startWork(): ListenableFuture<Result> {
        val data = Data.Builder()
        //sendDebugNotification(applicationContext, "Refreshing messages", "Refreshing your messages")

        setup()

        runBlocking {
            nl.tiebe.otarium.Data.selectedAccount.refreshMessages { title, message ->
                sendNotificationAndroid(
                    applicationContext,
                    title,
                    message
                )
            }

            sendDebugNotification(applicationContext, "Messages refreshed", "Your messages have been refreshed")
        }

        return Futures.immediateFuture(Result.success(data.build()))
    }
}

fun sendDebugNotification(context: Context, title: String, message: String) {
    if (nl.tiebe.otarium.Data.debugNotifications) {
        sendNotificationAndroid(context, title, message)
    }
}

actual fun sendNotification(title: String, message: String) {
    sendNotificationAndroid(Android.context, title, message)
}

fun sendNotificationAndroid(context: Context, title: String, message: String) {
    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
    intent?.action = ".MainActivity"
    intent?.flags = 0
    val builder = NotificationCompat.Builder(context, "grades")
        .setSmallIcon(R.drawable.ic_notification_foreground)
        .setContentTitle(title)
        .setPriority(1)
        .setAutoCancel(true)
        .setContentIntent(
            PendingIntent.getActivity(
                context,
                0,
                intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) FLAG_MUTABLE else FLAG_CANCEL_CURRENT
            )
        )
        .setGroup(System.currentTimeMillis().toString())
        .setColor(Color(nl.tiebe.otarium.Data.customDarkTheme.primary).toArgb())

    if (message != "") {
        builder.setContentText(message)
    }

    if (ActivityCompat.checkSelfPermission(
            context,
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
    NotificationManagerCompat.from(context).notify(System.currentTimeMillis().toInt(), builder.build())
}
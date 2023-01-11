package nl.tiebe.otarium.utils

import android.content.Context
import android.os.Build
import androidx.work.*
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.runBlocking
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
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val backgroundRequest = PeriodicWorkRequest.Builder(GradeRefreshWorker::class.java, 15, TimeUnit.MINUTES).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()
        ).build()

        WorkManager.getInstance(Android.context).enqueueUniquePeriodicWork("grades", ExistingPeriodicWorkPolicy.REPLACE, backgroundRequest)
    } else {
        //TODO: android version below oreo
    }
}

class TokenRefreshWorker(appContext: Context, workerParams: WorkerParameters): ListenableWorker(appContext, workerParams) {
    override fun startWork(): ListenableFuture<Result> {
        runBlocking {
            refreshTokens()
        }

        return Futures.immediateFuture(Result.success())
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

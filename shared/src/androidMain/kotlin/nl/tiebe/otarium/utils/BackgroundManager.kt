package nl.tiebe.otarium.utils

import android.content.Context
import android.os.Build
import androidx.work.*
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import io.ktor.client.call.*
import kotlinx.coroutines.runBlocking
import nl.tiebe.magisterapi.api.requestGET
import nl.tiebe.otarium.MAGISTER_TOKENS_URL
import nl.tiebe.otarium.magister.Tokens
import nl.tiebe.otarium.utils.server.MagisterTokenResponse
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

class TokenRefreshWorker(appContext: Context, workerParams: WorkerParameters): ListenableWorker(appContext, workerParams) {
    override fun startWork(): ListenableFuture<Result> {
        runBlocking {
            requestGET(MAGISTER_TOKENS_URL, hashMapOf(), Tokens.getPastTokens()?.accessTokens?.accessToken ?: return@runBlocking).body<MagisterTokenResponse>().also { Tokens.saveMagisterTokens(it) }
        }

        return Futures.immediateFuture(Result.success())
    }

}

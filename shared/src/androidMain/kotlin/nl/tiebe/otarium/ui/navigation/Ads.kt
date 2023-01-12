package nl.tiebe.otarium.ui.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import nl.tiebe.otarium.BuildConfig
import nl.tiebe.otarium.ageOfConsent

@Composable
internal actual fun Ads() {
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            MobileAds.initialize(context) {}

            AdView(context).apply {
                adUnitId =
                    if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544~3347511713" // test ads
                    else "ca-app-pub-1684141915170982/7736101438" // prod ads
                setAdSize(AdSize.BANNER)

                val requestConfiguration = MobileAds.getRequestConfiguration()
                    .toBuilder()
                    .setTagForChildDirectedTreatment(ageOfConsent().compareTo(false))
                    .setTagForUnderAgeOfConsent(ageOfConsent().compareTo(false))
                    .build()
                MobileAds.setRequestConfiguration(requestConfiguration)

                loadAd(AdRequest.Builder().build())
            }
        }
    )
}
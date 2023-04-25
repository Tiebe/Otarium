package nl.tiebe.otarium.ui.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.unit.dp
import cocoapods.Google_Mobile_Ads_SDK.GADAdSizeBanner
import cocoapods.Google_Mobile_Ads_SDK.GADMobileAds
import cocoapods.Google_Mobile_Ads_SDK.GAMBannerView
import cocoapods.Google_Mobile_Ads_SDK.GAMRequest
import kotlinx.cinterop.cValue
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.utils.ui.IOS
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

@Composable
internal actual fun Ads() {
    UIKitView(
        factory = {
            GADMobileAds.sharedInstance().requestConfiguration.tagForChildDirectedTreatment(!Data.ageOfConsent)
            GADMobileAds.sharedInstance().requestConfiguration.tagForUnderAgeOfConsent(!Data.ageOfConsent)
            GADMobileAds.sharedInstance().requestConfiguration.testDeviceIdentifiers = listOf("de4771839454b2759a902a8882e8942b")

            GADMobileAds.sharedInstance().startWithCompletionHandler(null)

            val banner = GAMBannerView(adSize = cValue {GADAdSizeBanner})

            banner.adUnitID = "ca-app-pub-1684141915170982/3587740616" //debug
            //banner.adUnitID = "ca-app-pub-3940256099942544/2934735716" //prod
            banner.rootViewController = IOS.viewController
            banner.translatesAutoresizingMaskIntoConstraints = false

            banner.loadRequest(GAMRequest())

            banner
        },
        update = {
            it.loadRequest(GAMRequest())
        },
        modifier = Modifier.fillMaxWidth().height(50.dp),
        interactive = true
    )
}
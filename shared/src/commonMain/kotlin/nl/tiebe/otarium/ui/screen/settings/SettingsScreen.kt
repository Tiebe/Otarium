package nl.tiebe.otarium.ui.screen.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.ui.icons.BugOutline
import nl.tiebe.otarium.ui.navigation.adsShown
import nl.tiebe.otarium.ui.screen.settings.popups.BugReportPopup
import nl.tiebe.otarium.ui.screen.settings.popups.ChangeUserPopup
import nl.tiebe.otarium.utils.ui.getLocalizedString

//TODO complete redesign

@OptIn(DelicateCoroutinesApi::class)
@Composable
internal fun SettingsScreen(componentContext: ComponentContext, onNewUser: () -> Unit) {
    val currentPopup = remember { mutableStateOf<Pair<Boolean, (@Composable () -> Unit)?>>(false to null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(modifier = Modifier
            .fillMaxWidth(0.95f)
            .height(70.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {

            Text(text = getLocalizedString(MR.strings.switch_user_text),
                textAlign = TextAlign.Center)

            Button(modifier = Modifier.width(50.dp), onClick = {
                                                               currentPopup.value = true to {
                                                                   ChangeUserPopup(componentContext, {
                                                                       currentPopup.value = false to currentPopup.value.second
                                                                   }, onNewUser)
                                                               }
            }, contentPadding = PaddingValues(0.dp)) {
                Icon(Icons.Default.AccountCircle, "Account switch", modifier = Modifier.fillMaxWidth())
            }
        }

        Divider()

        Row(modifier = Modifier
            .fillMaxWidth(0.95f)
            .height(70.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            val checkedStateAds = remember { mutableStateOf(Data.showAds) }

            Text(text = getLocalizedString(MR.strings.show_ads_checkbox),
                textAlign = TextAlign.Center)

            Switch(checked = checkedStateAds.value, onCheckedChange = {
                checkedStateAds.value = it
                Data.showAds = it
                adsShown = it
            })
        }

        Divider()

        Row(modifier = Modifier
            .fillMaxWidth(0.95f)
            .height(70.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            val checkedStateAge = remember { mutableStateOf(Data.ageOfConsent) }

            Text(text = getLocalizedString(MR.strings.age_checkbox),
            textAlign = TextAlign.Center)

            Switch(checked = checkedStateAge.value, onCheckedChange = {
               GlobalScope.launch {
                    checkedStateAge.value = it
                    Data.ageOfConsent = it
                    adsShown = false
                    delay(500)
                    adsShown = Data.showAds
                }
            })
        }

        Divider()

        Row(modifier = Modifier
            .fillMaxWidth(0.95f)
            .height(70.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
            Text(text = getLocalizedString(MR.strings.bug_report),
                textAlign = TextAlign.Center)

            Button(modifier = Modifier.width(50.dp), onClick = {
                currentPopup.value = true to {
                    BugReportPopup(componentContext) {
                        currentPopup.value = false to currentPopup.value.second
                    }
                }
            }, contentPadding = PaddingValues(0.dp)) {
                Icon(BugOutline, "Bug", modifier = Modifier.fillMaxWidth()) }
        }

        Divider()
    }


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AnimatedVisibility(
            visible = currentPopup.value.first,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            currentPopup.value.second?.invoke()
        }
    }
}

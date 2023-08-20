package nl.tiebe.otarium.androidApp.ui.home.debug

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.androidApp.*
import nl.tiebe.otarium.androidApp.ui.home.settings.utils.SettingRowIconButton
import nl.tiebe.otarium.androidApp.ui.home.settings.utils.SettingsRowToggle
import nl.tiebe.otarium.logic.root.home.children.debug.DebugComponent
import nl.tiebe.otarium.magister.refreshGrades
import nl.tiebe.otarium.utils.OtariumIcons
import nl.tiebe.otarium.utils.otariumicons.BugOutline
import kotlin.random.Random

@Composable
internal fun DebugScreen(component: DebugComponent) {
    val context = LocalContext.current
    Column(modifier = Modifier.padding(horizontal = 8.dp).verticalScroll(rememberScrollState())) {
        SettingRowIconButton(
            leftText = AnnotatedString("Ask notification permission"),
            icon = Icons.Default.Notifications,
            rowClickable = true,
        ) {
            setupNotifications(context)
        }

        SettingRowIconButton(
            leftText = AnnotatedString("Send test notification"),
            icon = Icons.Default.Notifications,
            rowClickable = true,
        ) {
            sendNotification(context, "Test notification", "This is a test notification")
        }

        SettingRowIconButton(
            leftText = AnnotatedString("Remove random grade"),
            icon = Icons.Default.Delete,
            rowClickable = true,
        ) {
            val account = Data.selectedAccount
            val savedGrades = account.fullGradeList.toMutableList()

            savedGrades.removeAt(Random.nextInt(savedGrades.size))
            account.fullGradeList = savedGrades
        }

        SettingRowIconButton(
            leftText = AnnotatedString("Check grades"),
            icon = Icons.Default.Refresh,
            rowClickable = true,
        ) {
            component.scope.launch {
                Data.selectedAccount.refreshGrades { title, text ->
                    sendNotification(context, title, text)
                }
            }
        }

        SettingRowIconButton(
            leftText = AnnotatedString("Check grades background"),
            icon = Icons.Default.Refresh,
            rowClickable = true,
        ) {
            component.scope.launch {
                setupGradesBackgroundTask(context)
            }
        }

        SettingRowIconButton(
            leftText = AnnotatedString("Export accounts"),
            icon = Icons.Default.Share,
            rowClickable = true,
        ) {
            component.exportAccounts()
        }


        SettingRowIconButton(
            leftText = AnnotatedString("Import accounts"),
            icon = Icons.Default.Add,
            rowClickable = true,
        ) {
            component.importAccounts(getClipboardText(context))
        }

        SettingRowIconButton(
            leftText = AnnotatedString("Change language"),
            icon = Icons.Default.KeyboardArrowRight,
            rowClickable = true,
        ) {
            component.changeLanguage()
        }

        SettingRowIconButton(
            leftText = AnnotatedString("Test crash"),
            icon = OtariumIcons.BugOutline,
            rowClickable = true,
        ) {
            throw RuntimeException("Test crash")
        }

        SettingRowIconButton(
            leftText = AnnotatedString("Refresh tokens"),
            icon = Icons.Default.Refresh,
            rowClickable = true,
        ) {
            component.scope.launch {
                for (account in Data.accounts) {
                    account.refreshTokens()
                }
            }
        }

        val checked = remember { mutableStateOf(Data.debugNotifications) }

        SettingsRowToggle(
            leftText = AnnotatedString("Debug notifications"),
            rowClickable = true,
            checked = checked.value,
        ) {
            checked.value = it
            Data.debugNotifications = it
        }

        SettingRowIconButton(
            leftText = AnnotatedString("Invalidate tokens"),
            icon = OtariumIcons.BugOutline,
            rowClickable = true,
        ) {
            //todo
            //Data.selectedAccount.tokens = settings.getStringOrNull("tokens-${Data.selectedAccount.accountId}")?.let { Json.decodeFromString<TokenResponse?>(it)?.copy(accessToken = "asdsa", refreshToken = "asdsad") }!!
        }

        SettingRowIconButton(
            leftText = AnnotatedString("Refresh messages"),
            icon = Icons.Default.Refresh,
            rowClickable = true,
        ) {
            component.scope.launch {
                setupMessagesBackgroundTask(context)
            }
        }
    }

}
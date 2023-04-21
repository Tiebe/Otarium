package nl.tiebe.otarium.ui.home.debug

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.magister.refreshGrades
import nl.tiebe.otarium.setupNotifications
import nl.tiebe.otarium.ui.home.settings.utils.SettingRowIconButton
import nl.tiebe.otarium.utils.getClipboardText
import nl.tiebe.otarium.utils.refreshGradesBackground
import nl.tiebe.otarium.utils.sendNotification
import kotlin.random.Random

@Composable
internal fun DebugScreen(component: DebugComponent) {
    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        SettingRowIconButton(
            leftText = AnnotatedString("Ask notification permission"),
            icon = Icons.Default.Notifications
        ) {
            setupNotifications()
        }

        SettingRowIconButton(
            leftText = AnnotatedString("Send test notification"),
            icon = Icons.Default.Notifications
        ) {
            sendNotification("Test notification", "This is a test notification")
        }

        SettingRowIconButton(
            leftText = AnnotatedString("Remove random grade"),
            icon = Icons.Default.Delete
        ) {
            val account = Data.selectedAccount
            val savedGrades = account.fullGradeList.toMutableList()

            savedGrades.removeAt(Random.nextInt(savedGrades.size))
            account.fullGradeList = savedGrades
        }

        SettingRowIconButton(
            leftText = AnnotatedString("Check grades"),
            icon = Icons.Default.Refresh
        ) {
            component.scope.launch {
                Data.selectedAccount.refreshGrades()
            }
        }

        SettingRowIconButton(
            leftText = AnnotatedString("Check grades background"),
            icon = Icons.Default.Refresh
        ) {
            component.scope.launch {
                refreshGradesBackground()
            }
        }

        SettingRowIconButton(
            leftText = AnnotatedString("Export accounts"),
            icon = Icons.Default.Share
        ) {
            component.exportAccounts()
        }


        SettingRowIconButton(
            leftText = AnnotatedString("Import accounts"),
            icon = Icons.Default.Add
        ) {
            component.importAccounts(getClipboardText())
        }
    }

}
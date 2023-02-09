package nl.tiebe.otarium.ui.screen.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.utils.ui.CBackHandler
import nl.tiebe.otarium.utils.ui.getLocalizedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChangeUserScreen(componentContext: ComponentContext, onExit: () -> Unit, onNewUser: () -> Unit) {
    CBackHandler(componentContext, onBack = onExit)

    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

        //todo: show currently selected account
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp))
        {
            var currentlySelected by remember { mutableStateOf(Data.selectedAccount.accountId) }

            Data.accounts.forEach { account ->
                val info = account.profileInfo
                val fullName = "${info.person.firstName}${if (info.person.preposition == null) "" else " ${info.person.preposition}"} ${info.person.lastName}"

                ListItem(
                    headlineText = { Text(fullName) },
                    modifier = Modifier.clickable { Data.selectedAccount = account; currentlySelected = account.accountId },
                    colors = ListItemDefaults.colors(containerColor = if (currentlySelected == account.accountId) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background)
                )
            }

            ListItem(
                headlineText = { Text(getLocalizedString(MR.strings.new_account)) },
                modifier = Modifier.clickable {
                    onNewUser()
                }
            )
        }
    }
}
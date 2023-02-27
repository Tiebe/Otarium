package nl.tiebe.otarium.ui.screen.settings.popups

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.settings
import nl.tiebe.otarium.utils.ui.CBackHandler
import nl.tiebe.otarium.utils.ui.getLocalizedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChangeUserPopup(componentContext: ComponentContext, onExit: () -> Unit, onNewUser: () -> Unit) {
    CBackHandler(componentContext, onBack = onExit)

    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp))
        {
            var currentlySelected by remember { mutableStateOf(Data.selectedAccount.accountId) }
            var currentAccounts by remember { mutableStateOf(Data.accounts) }

            currentAccounts.forEach { account ->
                val info = account.profileInfo
                val fullName = "${info.person.firstName}${if (info.person.preposition == null) "" else " ${info.person.preposition}"} ${info.person.lastName}"

                ListItem(
                    headlineText = { Text(fullName) },
                    trailingContent = { Icon(Icons.Default.Delete, "Remove account", modifier = Modifier.clickable {
                        settings.remove("agenda-${account.accountId}")
                        settings.remove("grades-${account.accountId}")
                        settings.remove("full_grade_list-${account.accountId}")
                        settings.remove("tokens-${account.accountId}")
                        Data.accounts = Data.accounts.filter { it.accountId != account.accountId }
                        currentAccounts = Data.accounts

                        if (currentAccounts.isEmpty()) {
                            onNewUser()
                        }
                        else if (Data.selectedAccount.accountId == account.accountId) {
                            Data.selectedAccount = Data.accounts.first { it.accountId != account.accountId }
                        }

                    }) },
                    modifier = Modifier.clickable { Data.selectedAccount = account; currentlySelected = account.accountId },
                    colors = ListItemDefaults.colors(containerColor = if (currentlySelected == account.accountId) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background)
                )
            }
        }

        Box(
            Modifier
                .size(56.dp)
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = onNewUser,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, getLocalizedString(MR.strings.new_account))
            }
        }


    }
}
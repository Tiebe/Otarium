package nl.tiebe.otarium.ui.home.settings.items.users

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.utils.ui.getLocalizedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UserChildScreen(component: UserChildComponent) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        var currentlySelected = component.selectedAccount.subscribeAsState().value
        var currentAccounts = component.users.subscribeAsState().value

        currentAccounts.forEach { account ->
            val info = account.profileInfo
            val fullName = "${info.person.firstName}${if (info.person.preposition == null) "" else " ${info.person.preposition}"} ${info.person.lastName}"

            ListItem(
                headlineText = { Text(fullName) },
                trailingContent = { Icon(Icons.Default.Delete, "Remove account", modifier = Modifier.clickable {
                    component.removeAccount(account.accountId)
                }) },
                modifier = Modifier.clickable { component.selectAccount(account) },
                colors = ListItemDefaults.colors(containerColor = if (currentlySelected == account.accountId) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background)
            )
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = component.openLoginScreen,
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Default.Add, getLocalizedString(MR.strings.new_account))
        }
    }
}
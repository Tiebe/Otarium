package dev.tiebe.otarium.ui.home.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.tiebe.otarium.Data
import dev.tiebe.otarium.MR
import dev.tiebe.otarium.logic.root.home.children.settings.SettingsComponent
import dev.tiebe.otarium.magister.MagisterAccount
import dev.tiebe.otarium.ui.home.settings.items.main.MainChildScreen
import dev.tiebe.otarium.ui.home.settings.items.ui.UIChildScreen
import dev.tiebe.otarium.ui.home.settings.items.ui.colors.ColorChildScreen
import dev.tiebe.otarium.utils.convertImageByteArrayToBitmap
import dev.tiebe.otarium.utils.ui.getLocalizedString

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun SettingsScreen(component: SettingsComponent) {
    val screen = component.childStack.subscribeAsState()
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.padding(start = 5.dp, end = 5.dp)) {
        Children(
            stack = screen.value,
            animation = stackAnimation(slide())
        ) {
            SettingsScreenChild(component, it)
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BoxScope.SettingsScreenChild(
    component: SettingsComponent,
    screen: Child.Created<SettingsComponent.Config, SettingsComponent.Child>
) {
    Column {
        TopAppBar(
            title = { Text(screen.configuration.localizedString) },
            navigationIcon = {
                if (screen.instance !is SettingsComponent.Child.MainChild) {
                    IconButton(onClick = { component.back() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            },
            windowInsets = WindowInsets(0.dp)
        )

        Box(modifier = Modifier.padding(start = 5.dp, end = 5.dp)) {
            when (val child = screen.instance) {
                is SettingsComponent.Child.MainChild -> MainChildScreen(child.component)
                is SettingsComponent.Child.UIChild -> UIChildScreen(child.component)
                is SettingsComponent.Child.ColorChild -> ColorChildScreen(child.component)
            }
        }
    }

    if (screen.instance is SettingsComponent.Child.MainChild) {
        UserFAB(component, Modifier.align(Alignment.TopEnd))
    }
}

@Composable
fun UserFAB(component: SettingsComponent, modifier: Modifier) {
    var expanded by remember { mutableStateOf(false) }

    FloatingActionButton(
        onClick = { expanded = !expanded },
        shape = CircleShape,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(16.dp).size(40.dp).then(modifier),
    ) {
        Image(
            bitmap = convertImageByteArrayToBitmap(Data.selectedAccount.profileImage),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }

    Data.accounts.forEachIndexed { index, account ->
        AnimatedVisibility(visible = expanded, modifier.offset(y = (index + 1) * 50.dp), enter = expandIn()) {
            AccountFab(account) {
                Data.selectedAccount = account
                expanded = false
            }
        }
    }

    AnimatedVisibility(visible = expanded, modifier = modifier.offset(y = (Data.accounts.size + 1) * 50.dp), enter = expandIn()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(getLocalizedString(MR.strings.add_account))

            Spacer(Modifier.width(8.dp))

            FloatingActionButton(
                shape = CircleShape,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = { component.openLoginScreen(); expanded = false },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(Icons.Default.Add, getLocalizedString(MR.strings.add_account), tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
fun AccountFab(account: MagisterAccount, onClick: () -> Unit) {
    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(account.profileInfo.person.firstName)

        Spacer(Modifier.width(8.dp))

        FloatingActionButton(
            shape = CircleShape,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary,
            onClick = onClick,
            modifier = Modifier.size(40.dp)
        ) {
            Image(
                bitmap = convertImageByteArrayToBitmap(account.profileImage),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
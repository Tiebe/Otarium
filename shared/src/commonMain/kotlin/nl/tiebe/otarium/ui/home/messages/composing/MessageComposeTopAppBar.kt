package nl.tiebe.otarium.ui.home.messages.composing

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import nl.tiebe.otarium.logic.root.home.children.messages.children.composing.MessageComposeComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageComposeTopAppBar(component: MessageComposeComponent) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = { component.parentComponent.back() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            // send
            IconButton(onClick = { component.send() }) {
                Icon(Icons.Default.Send, contentDescription = "Send")
            }
        },
        windowInsets = WindowInsets(0)
    )
}
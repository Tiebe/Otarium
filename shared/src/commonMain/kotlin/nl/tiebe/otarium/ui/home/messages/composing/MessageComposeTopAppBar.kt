package nl.tiebe.otarium.ui.home.messages.composing

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import nl.tiebe.otarium.logic.root.home.children.messages.children.composing.MessageComposeComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageComposeTopAppBar(component: MessageComposeComponent) {
    TopAppBar(
        title = { Text("Compose message REPLACE THIS", overflow = TextOverflow.Ellipsis, maxLines = 1) },
        navigationIcon = {
            IconButton(onClick = { component.parentComponent.back() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        windowInsets = WindowInsets(0)
    )
}
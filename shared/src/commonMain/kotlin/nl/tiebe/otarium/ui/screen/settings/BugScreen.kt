package nl.tiebe.otarium.ui.screen.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.navigation.BackHandler

// TODO make it look better

@Composable
internal fun BugScreen(onExit: () -> Unit) {
    BackHandler(onBack = onExit)

    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp))
        {
            Row(modifier = Modifier
                .fillMaxWidth(0.95f)
                .height(70.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Clear cache", textAlign = TextAlign.Center)
                Button(onClick = { deleteCache() }) {
                    Text("Clear")
                }
            }
        }
    }
}

expect fun deleteCache()
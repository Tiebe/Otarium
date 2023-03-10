package nl.tiebe.otarium.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nl.tiebe.otarium.oldui.navigation.Ads
import nl.tiebe.otarium.oldui.navigation.adsShown

@Composable
internal fun HomeScreen(component: HomeComponent) {
    Column(modifier = Modifier.fillMaxSize()) {
        BottomBar(
            component,
            Modifier.padding(bottom = if (adsShown) 50.dp else 0.dp)
        )

        if (adsShown) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                Ads()
            }
        }
    }
}

@Composable
internal expect fun Ads()
package nl.tiebe.otarium.androidApp.ui.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun OverlayButton(modifier: Modifier = Modifier, icon: @Composable RowScope.() -> Unit, onClick: () -> Unit) {
    Button(
        modifier = Modifier.size(30.dp).then(modifier),
        onClick = onClick,
        contentPadding = PaddingValues(0.dp),
        content = icon
    )
}
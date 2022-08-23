package nl.tiebe.openbaarlyceumzeist.android.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MainMenu(
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        Text(text = "MainMenu")
    }
}

@Preview(name = "MainMenu")
@Composable
private fun PreviewMainMenu() {
    MainMenu()
}
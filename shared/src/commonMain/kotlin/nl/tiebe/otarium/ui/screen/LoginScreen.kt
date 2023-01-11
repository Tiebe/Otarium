package nl.tiebe.otarium.ui.screen


import androidx.compose.runtime.Composable

@Composable
internal expect fun LoginScreen(onLogin: (Pair<Boolean, Pair<String, String?>>) -> Unit)


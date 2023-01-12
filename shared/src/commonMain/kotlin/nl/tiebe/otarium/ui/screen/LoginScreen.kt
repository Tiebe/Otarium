package nl.tiebe.otarium.ui.screen


import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext

@Composable
internal expect fun LoginScreen(componentContext: ComponentContext, onLogin: (Pair<Boolean, Pair<String, String?>>) -> Unit)
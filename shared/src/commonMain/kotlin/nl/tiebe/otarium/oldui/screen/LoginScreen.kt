package nl.tiebe.otarium.oldui.screen


import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import nl.tiebe.otarium.magister.MagisterLogin

@Composable
internal expect fun LoginScreen(componentContext: ComponentContext, onLogin: (MagisterLogin) -> Unit)
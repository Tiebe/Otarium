package nl.tiebe.otarium.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
internal actual fun LoginScreen(componentContext: ComponentContext, onLogin: (Pair<Boolean, Pair<String, String?>>) -> Unit) {
    Column {
        var code by remember { mutableStateOf("") }
        val failed by remember { mutableStateOf(false) }

        if (failed) {
            Text("Failed to login")
        }


        Text("Please enter the code you received from the companion app:")
        TextField(value = code, onValueChange = { code = it })
        Button(onClick = {
            runBlocking {
                launch {
                    try {
                        onLogin(true to (code to ""))
                    } catch (e: Exception) {
                        e.printStackTrace()


                    }
                }
            }
            //TODO: send firebase token from ios
        }) {
            Text("Login")
        }
    }
}

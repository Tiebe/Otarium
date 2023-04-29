package nl.tiebe.otarium.ui.login

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal actual fun LoginScreen(component: LoginComponent) {
    Text(component.loginUrl.url)

    val textFieldValue = remember { mutableStateOf(TextFieldValue()) }

    TextField(textFieldValue.value, onValueChange = {
        textFieldValue.value = it
    })

    val scope = rememberCoroutineScope()

    Button(
        onClick = {
            scope.launch {
                component.login(textFieldValue.value.text, component.loginUrl.codeVerifier)
            }
        }
    ) {
        Text("Login")
    }
}
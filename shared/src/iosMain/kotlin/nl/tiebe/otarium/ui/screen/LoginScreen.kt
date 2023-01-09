package nl.tiebe.otarium.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import io.ktor.client.call.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.tiebe.magisterapi.api.requestGET
import nl.tiebe.magisterapi.api.requestPOST
import nl.tiebe.otarium.CODE_EXCHANGE_URL
import nl.tiebe.otarium.EXCHANGE_URL
import nl.tiebe.otarium.magister.Tokens
import nl.tiebe.otarium.utils.server.LoginResponse

@Composable
internal actual fun LoginScreen(onLogin: () -> Unit) {
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
                        exchangeOTP(code)

                        onLogin()
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

suspend fun exchangeOTP(otp: String): LoginResponse {
    val response = requestGET(CODE_EXCHANGE_URL, hashMapOf("code" to otp)).body<LoginResponse>()

    Tokens.saveTokens(response)

    return response
}
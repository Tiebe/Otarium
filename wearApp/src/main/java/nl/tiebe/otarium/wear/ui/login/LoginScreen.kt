package nl.tiebe.otarium.wear.ui.login

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat.startActivity
import androidx.wear.activity.ConfirmationActivity
import androidx.wear.compose.material3.Text
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.Wearable
import nl.tiebe.otarium.logic.root.login.LoginComponent


@Composable
internal fun LoginScreen(component: LoginComponent) {
    val context = LocalContext.current

    var text by remember { mutableStateOf("Waiting for sign in on phone...") }

    Thread {
        Wearable.getMessageClient(context).addListener {
            println("Got message! ")
            if (it.path == "/login_completed") {
                component.checkUrl(it.data.decodeToString())
            } else if (it.path == "/login_bypass") {
                component.bypassLogin()
            }
        }

        var node = Tasks.await(
            Wearable.getNodeClient(context).connectedNodes
        ).firstOrNull()

        while (node == null) {
            text = "No connected phones..."

            node = Tasks.await(
                Wearable.getNodeClient(context).connectedNodes
            ).firstOrNull()
        }

        text = "Waiting for sign in on phone..."

        Wearable.getMessageClient(context).sendMessage(node.id, "/wear_login", component.loginUrl.url.toByteArray())

    }.start()

    val intent = Intent(context, ConfirmationActivity::class.java)

    intent.putExtra(
        ConfirmationActivity.EXTRA_ANIMATION_TYPE,
        ConfirmationActivity.OPEN_ON_PHONE_ANIMATION
    )

    startActivity(context, intent, null)

    Box(modifier = Modifier.fillMaxSize()) {
        Text(text, modifier = Modifier.align(Alignment.Center), textAlign = TextAlign.Center)
    }

}
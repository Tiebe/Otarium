package nl.tiebe.otarium.wear.ui.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.Wearable
import nl.tiebe.otarium.logic.root.login.LoginComponent

@Composable
internal fun LoginScreen(component: LoginComponent) {
    val context = LocalContext.current

    Thread {
        Wearable.getMessageClient(context).addListener {
            println("Got message! ")
            if (it.path == "/login_completed") {
                component.checkUrl(it.data.decodeToString())
            } else if (it.path == "/login_bypass") {
                component.bypassLogin()
            }
        }

        val node = Tasks.await(
            Wearable.getNodeClient(context).connectedNodes
        ).first()

        Wearable.getMessageClient(context).sendMessage(node.id, "/wear_login", component.loginUrl.url.toByteArray())

    }.start()



}
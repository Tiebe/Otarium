package nl.tiebe.otarium.androidApp.ui.home.timetable.item

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import dev.tiebe.magisterapi.response.profileinfo.Contact
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.logic.root.home.children.other.contact.ContactComponent

@Composable
fun ContactScreen(component: ContactComponent) {
    val scope = rememberCoroutineScope()
    val contact = component.contact.subscribeAsState().value

    Surface(modifier = Modifier.fillMaxSize()) {
        when (contact.id) {
            0 -> LinearProgressIndicator()
            -1 -> Text(stringResource(MR.strings.contact_not_found.resourceId))
            else -> {
                Text((contact.roepnaam ?: contact.voorletters) + " ${contact.tussenvoegsel?.plus(" ") ?: ""}" + contact.achternaam)
            }
        }
    }

}


@Preview
@Composable
fun ContactScreenPreview() {
    val previewComponent: ContactComponent = object : ContactComponent {
        override val contact: MutableValue<Contact> = MutableValue(Contact(1, "T.L.D.", "van", "Groosman", "tgro", "leerling", roepnaam = "Tiebe"))
        override suspend fun getContactInfo(name: String): Contact? = null
    }

    ContactScreen(previewComponent)
}
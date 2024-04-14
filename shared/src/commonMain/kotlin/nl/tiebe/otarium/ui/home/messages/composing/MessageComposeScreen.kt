package nl.tiebe.otarium.ui.home.messages.composing

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.tiebe.magisterapi.response.profileinfo.Contact
import nl.tiebe.otarium.logic.root.home.children.messages.children.composing.MessageComposeComponent
import nl.tiebe.otarium.ui.utils.AutoCompleteTextView
import nl.tiebe.otarium.ui.utils.ContactChip
import nl.tiebe.otarium.ui.utils.chips.rememberChipTextFieldState

@Composable
fun MessageComposeScreen(component: MessageComposeComponent) {
    Column(Modifier.fillMaxSize()) {
        ToInputField(component)
        SubjectInputField(component)

        /*        CcInputField(component)
                BccInputField(component)
                BodyInputField(component)
                SendButton(component)*/
    }
}


@Composable
fun ToInputField(component: MessageComposeComponent) {
    Box(Modifier.padding(start= 16.dp, end = 16.dp, top = 24.dp)) {
        var query by remember { mutableStateOf("") }
        val state = rememberChipTextFieldState(emptyList<ContactChip>())

        AutoCompleteTextView(
            query = query,
            onQueryChanged = { query = it },
            predictions = component.contactList.subscribeAsState().value.filter {
                if (query.isEmpty()) return@filter true
                getName(it).contains(query, ignoreCase = true)
            },
            queryLabel = "To",
            itemContent = { Text(getName(it)) },
            onItemClick = {
                state.addChip(ContactChip(it))
                query = ""
            },
            state = state
        )
    }

}

fun getName(contact: Contact): String {
    var searchTerm =
        "${contact.roepnaam ?: contact.voorletters} ${contact.tussenvoegsel?.plus(" ") ?: ""}${contact.achternaam}"
    if (contact.klas != null) {
        searchTerm += " (${contact.klas})"
    }

    return searchTerm
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectInputField(component: MessageComposeComponent) {
    var subjectText by remember { mutableStateOf("") }

    Box(Modifier.padding(start= 16.dp, end = 16.dp, top = 24.dp)) {
        OutlinedTextField(
            value = subjectText,
            onValueChange = { subjectText = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Subject") },
            singleLine = true,

        )
    }

}

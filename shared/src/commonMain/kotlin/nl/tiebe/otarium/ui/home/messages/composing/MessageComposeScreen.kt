package nl.tiebe.otarium.ui.home.messages.composing

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.mohamedrejeb.richeditor.ui.material3.OutlinedRichTextEditor
import dev.tiebe.magisterapi.response.profileinfo.Contact
import nl.tiebe.otarium.logic.root.home.children.messages.children.composing.MessageComposeComponent
import nl.tiebe.otarium.ui.utils.AutoCompleteTextView
import nl.tiebe.otarium.ui.utils.ContactChip
import nl.tiebe.otarium.ui.utils.chips.ChipTextFieldState

@Composable
fun MessageComposeScreen(component: MessageComposeComponent) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        ToInputField(component, component.toList, "To")
        ToInputField(component, component.ccList, "CC")
        ToInputField(component, component.bccList, "BCC")

        SubjectInputField(component)

        BodyInputField(component)
    }
}


@Composable
fun ToInputField(component: MessageComposeComponent, state: ChipTextFieldState<ContactChip>, label: String) {
    Box(Modifier.padding(start= 16.dp, end = 16.dp)) {
        var query by remember { mutableStateOf("") }

        AutoCompleteTextView(
            query = query,
            onQueryChanged = { query = it },
            predictions = component.contactList.subscribeAsState().value.filter { contact ->
                if (query.isEmpty() || state.chips.any { it.contact == contact }) return@filter false
                getName(contact).contains(query, ignoreCase = true)
            }.sortedBy { getName(it) },
            queryLabel = label,
            itemContent = { Text(getName(it)) },
            onItemClick = { clickedItem ->
                if (state.chips.any { it.contact == clickedItem }) return@AutoCompleteTextView
                state.addChip(ContactChip(clickedItem))
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

@Composable
fun SubjectInputField(component: MessageComposeComponent) {
    Box(Modifier.padding(start = 16.dp, end = 16.dp)) {
        OutlinedTextField(
            value = component.subject.subscribeAsState().value,
            onValueChange = { component.subject.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Subject") },
            singleLine = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyInputField(component: MessageComposeComponent) {
    Column(Modifier.padding(top = 16.dp)) {
        RichTextStyleRow(
            state = component.body,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedRichTextEditor(
            state = component.body,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            label = {
                Text("Body")
            }
        )
    }
}


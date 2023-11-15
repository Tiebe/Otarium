package nl.tiebe.otarium.ui.home.messages.composing

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.logic.root.home.children.messages.children.composing.MessageComposeComponent
import nl.tiebe.otarium.ui.utils.AutoCompleteTextView

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
    var toText by remember { mutableStateOf("") }

    Box(Modifier.padding(start= 16.dp, end = 16.dp, top = 24.dp)) {
        var query by remember { mutableStateOf("") }

        AutoCompleteTextView(
            query = query,
            onQueryChanged = { query = it },
            predictions = component.contactList.subscribeAsState().value.map { it }, //todo
            queryLabel = "Test"
        )
    }

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

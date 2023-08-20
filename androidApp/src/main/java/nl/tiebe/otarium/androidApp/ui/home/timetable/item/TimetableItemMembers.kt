package nl.tiebe.otarium.androidApp.ui.home.timetable.item

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.tiebe.magisterapi.response.profileinfo.Contact
import nl.tiebe.otarium.logic.root.home.children.timetable.children.timetable.TimetableComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableItemMembers(component: TimetableComponent, id: Int) {
    val agendaItemWithAbsence = component.timetable.subscribeAsState().value.first { it.agendaItem.id == id }
    var contacts: List<Contact> by remember { mutableStateOf(emptyList()) }

    LaunchedEffect(agendaItemWithAbsence) {
        contacts = component.getClassMembers(agendaItemWithAbsence)
    }


    Surface(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp)) {
            LazyColumn {
                items(contacts.size) { contactIndex ->
                    ListItem(headlineText = {
                        Text(contacts[contactIndex].roepnaam ?: "")
                    })

                }
            }
        }
    }

}
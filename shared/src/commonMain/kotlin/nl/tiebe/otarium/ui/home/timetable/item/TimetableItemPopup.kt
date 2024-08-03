package nl.tiebe.otarium.ui.home.timetable.item

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import nl.tiebe.otarium.logic.root.home.children.timetable.children.timetable.TimetableComponent
import nl.tiebe.otarium.magister.AgendaItemWithAbsence
import nl.tiebe.otarium.ui.utils.DownloadIndicator
import nl.tiebe.otarium.utils.OtariumIcons
import nl.tiebe.otarium.utils.otariumicons.Email
import nl.tiebe.otarium.utils.otariumicons.email.Attachment
import nl.tiebe.otarium.utils.otariumicons.email.AttachmentOff

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetablePopupTopAppBar(component: TimetableComponent, agendaItem: AgendaItemWithAbsence) {
    TopAppBar(
        title = {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    val startTime =
                        LocalDateTime.parse(agendaItem.agendaItem.start.substring(0, 26))
                            .toInstant(TimeZone.UTC)
                    val endTime = LocalDateTime.parse(agendaItem.agendaItem.einde.substring(0, 26))
                        .toInstant(TimeZone.UTC)

                    val localStartTime = startTime.toLocalDateTime(TimeZone.of("Europe/Amsterdam"))
                    val localEndTime = endTime.toLocalDateTime(TimeZone.of("Europe/Amsterdam"))

                    Text(
                        agendaItem.agendaItem.description ?: "",
                    )
                    val supportingPopupText = mutableListOf<String>()
                    if (!agendaItem.agendaItem.location.isNullOrEmpty()) supportingPopupText.add(
                        agendaItem.agendaItem.location!!
                    )
                    supportingPopupText.add(
                        "${
                            localStartTime.hour.toString().padStart(2, '0')
                        }:${localStartTime.minute.toString().padStart(2, '0')} - ${
                            localEndTime.hour.toString().padStart(2, '0')
                        }:${localEndTime.minute.toString().padStart(2, '0')}"
                    )


                    Text(
                        supportingPopupText.joinToString(" • "),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }

                AbsenceBox(absence = agendaItem.absence)
            }
        },
        navigationIcon = {
            IconButton(onClick = { component.parentComponent.back() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        windowInsets = WindowInsets(0)
    )
}

@Composable
internal fun TimetableItemPopup(agendaItem: AgendaItemWithAbsence, component: TimetableComponent) {
    Box(Modifier.fillMaxSize().padding(top = 8.dp, start = 16.dp, end = 16.dp)) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            val attachments = agendaItem.agendaItem.attachments

            if (attachments?.isNotEmpty() == true) {
                val scrollState = rememberScrollState()

                Row(modifier = Modifier.fillMaxWidth().horizontalScroll(scrollState)) {
                    for (attachment in attachments) {
                        ElevatedCard(onClick = { component.downloadAttachment(attachment) }, modifier = Modifier.height(70.dp).padding(10.dp)) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Row(
                                    modifier = Modifier.fillMaxSize().padding(10.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        //TODO: status checken
                                        if (attachment.status == 1) OtariumIcons.Email.Attachment else OtariumIcons.Email.AttachmentOff,
                                        contentDescription = "Attachment"
                                    )
                                    Text(text = attachment.naam, modifier = Modifier.padding(start = 10.dp))
                                }

                                DownloadIndicator(component.attachmentDownloadProgress.subscribeAsState().value[attachment.id] ?: 0f)
                            }
                        }
                    }

                }

                HorizontalDivider()
            }

            val text = (agendaItem.agendaItem.content ?: "")

            val state = rememberRichTextState()

            LaunchedEffect(text) {
                state.setHtml(text)
            }

            RichText(
                state
            )
        }
    }
}
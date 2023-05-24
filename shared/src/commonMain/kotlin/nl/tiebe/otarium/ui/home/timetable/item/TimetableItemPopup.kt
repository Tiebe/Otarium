package nl.tiebe.otarium.ui.home.timetable.item

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.ui.home.timetable.main.TimetableComponent
import nl.tiebe.otarium.ui.utils.BackButton
import nl.tiebe.otarium.ui.utils.ClickableText
import nl.tiebe.otarium.ui.utils.parseHtml
import nl.tiebe.otarium.utils.ui.getLocalizedString

@Composable
internal fun TimetableItemPopup(component: TimetableComponent, agendaItemId: Int, modifier: Modifier) {
    val agendaItemWithAbsence = component.timetable.subscribeAsState().value.first { it.agendaItem.id == agendaItemId }

    Surface(Modifier.fillMaxSize().then(modifier)) {
        Box(Modifier.fillMaxSize().padding(top = 8.dp, start = 16.dp, end = 16.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val startTime =
                    agendaItemWithAbsence.agendaItem.start.substring(0, 26).toLocalDateTime()
                        .toInstant(TimeZone.UTC)
                val endTime = agendaItemWithAbsence.agendaItem.einde.substring(0, 26).toLocalDateTime()
                    .toInstant(TimeZone.UTC)

                val localStartTime = startTime.toLocalDateTime(TimeZone.of("Europe/Amsterdam"))
                val localEndTime = endTime.toLocalDateTime(TimeZone.of("Europe/Amsterdam"))

                Text(
                    agendaItemWithAbsence.agendaItem.description ?: "",
                    style = MaterialTheme.typography.headlineSmall
                )
                val supportingPopupText = mutableListOf<String>()
                if (!agendaItemWithAbsence.agendaItem.location.isNullOrEmpty()) supportingPopupText.add(
                    agendaItemWithAbsence.agendaItem.location!!
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

                Divider(Modifier.padding(top = 8.dp, bottom = 8.dp))

                if (agendaItemWithAbsence.absence != null) {
                    Text(
                        getLocalizedString(MR.strings.absence),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        agendaItemWithAbsence.absence.description ?: "",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Divider(Modifier.padding(top = 8.dp, bottom = 8.dp))
                }

                val text = (agendaItemWithAbsence.agendaItem.content ?: "").parseHtml()

                ClickableText(
                    text = text,
                    modifier = Modifier.fillMaxSize(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Box(Modifier.fillMaxSize().padding(top = 10.dp, end = 5.dp)) {
                BackButton(
                    Modifier.align(Alignment.TopEnd),
                    {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Exit",
                            modifier = Modifier.rotate(45f)
                        )
                    }) {
                    component.closeItemPopup()
                }
            }

        }
    }
}
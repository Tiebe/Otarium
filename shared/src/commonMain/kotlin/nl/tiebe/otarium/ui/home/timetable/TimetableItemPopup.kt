package nl.tiebe.otarium.ui.home.timetable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import nl.tiebe.otarium.magister.AgendaItemWithAbsence
import nl.tiebe.otarium.utils.ui.parseHtml

@Composable
internal fun TimetableItemPopup(agendaItemWithAbsence: AgendaItemWithAbsence) {


    Surface(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
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

            Text(
                (agendaItemWithAbsence.agendaItem.content ?: "").parseHtml(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
package nl.tiebe.otarium.ui.home.timetable.item

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.dp
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import nl.tiebe.otarium.magister.AgendaItemWithAbsence
import nl.tiebe.otarium.ui.home.timetable.TimetableComponent
import nl.tiebe.otarium.ui.utils.BackButton
import nl.tiebe.otarium.ui.utils.parseHtml
import nl.tiebe.otarium.utils.openUrl

@Composable
internal fun TimetableItemPopup(component: TimetableComponent, agendaItemWithAbsence: AgendaItemWithAbsence) {
    Surface(Modifier.fillMaxSize()) {
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

                val text = (agendaItemWithAbsence.agendaItem.content ?: "").parseHtml()

                val onClick: (Int) -> Unit = { offset ->
                    text.getStringAnnotations(tag = "URL", start = offset, end = offset).firstOrNull()?.let { annotation ->
                        openUrl(annotation.item)
                    }
                }

                val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
                val pressIndicator = Modifier.pointerInput(onClick) {
                    detectTapGestures { pos ->
                        layoutResult.value?.let { layoutResult ->
                            onClick(layoutResult.getOffsetForPosition(pos))
                        }
                    }
                }

                Text(
                    text = text,
                    modifier = Modifier.fillMaxSize().then(pressIndicator),
                    onTextLayout = { layoutResult.value = it },
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
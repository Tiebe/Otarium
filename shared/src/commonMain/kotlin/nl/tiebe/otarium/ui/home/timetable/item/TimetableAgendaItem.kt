package nl.tiebe.otarium.ui.home.timetable.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import nl.tiebe.otarium.magister.AgendaItemWithAbsence
import nl.tiebe.otarium.oldui.utils.topBottomRectBorder
import nl.tiebe.otarium.ui.home.timetable.TimetableComponent
import nl.tiebe.otarium.ui.home.timetable.main.dpPerHour
import nl.tiebe.otarium.utils.ui.parseHtml

@Composable
internal fun TimetableAgendaItem(component: TimetableComponent, agendaItemWithAbsence: AgendaItemWithAbsence) {
    val agendaItem = agendaItemWithAbsence.agendaItem
    val absence = agendaItemWithAbsence.absence
    val startTime =
        agendaItem.start.substring(0, 26).toLocalDateTime().toInstant(TimeZone.UTC)
    val endTime =
        agendaItem.einde.substring(0, 26).toLocalDateTime().toInstant(TimeZone.UTC)

    val localStartTime = startTime.toLocalDateTime(TimeZone.of("Europe/Amsterdam"))
    val localEndTime = endTime.toLocalDateTime(TimeZone.of("Europe/Amsterdam"))

    val height =
        dpPerHour * ((endTime.toEpochMilliseconds() - startTime.toEpochMilliseconds()).toFloat() / 60 / 60 / 1000)
    var distanceAfterTop =
        (dpPerHour * ((startTime.toEpochMilliseconds() - timeTop).toFloat() / 60 / 60 / 1000))
    if (distanceAfterTop < 0.dp) distanceAfterTop = 0.dp

    val supportingText = mutableListOf<AnnotatedString>()

    if (!agendaItem.location.isNullOrEmpty()) supportingText.add(AnnotatedString(agendaItem.location!!))
    supportingText.add(
        AnnotatedString(
        "${
            localStartTime.hour.toString().padStart(2, '0')
        }:${localStartTime.minute.toString().padStart(2, '0')} - ${
            localEndTime.hour.toString().padStart(2, '0')
        }:${localEndTime.minute.toString().padStart(2, '0')}"
    )
    )

    if (!agendaItem.content.isNullOrEmpty()) supportingText.add(
        agendaItem.content!!.parseHtml()
    )

    ListItem(
        modifier = Modifier
            .padding(start = 40.5.dp, top = distanceAfterTop)
            .fillMaxWidth(1/(overlappingItems.second.toFloat()+1))
            .height(height)
            .topBottomRectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline))
            .clickable { component.openTimeTableItem(agendaItemWithAbsence) }.let {
                if (isMostOverlapping) {
                    it.align(Alignment.TopEnd)
                } else {
                    it
                }
            }
        ,
        headlineText = { Text(agendaItem.description ?: "") },
        supportingText = {
            Text(
                supportingText.joinToString(" • "),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingContent = {
            if (agendaItem.fromPeriod != null) {
                Text(agendaItem.fromPeriod!!.toString(), modifier = Modifier.padding(2.dp))
            } else {
                Spacer(modifier = Modifier.size(16.dp))
            }
        },
        trailingContent = {
            if (absence?.justified == true) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .size(25.dp)
                        .background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        absence.code.uppercase(),
                        modifier = Modifier.padding(2.dp),
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            } else if (absence?.justified == false) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .size(25.dp)
                        .background(MaterialTheme.colorScheme.tertiary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        absence.code.uppercase(),
                        modifier = Modifier.padding(2.dp),
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }
            }
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface
        ),
    )
}
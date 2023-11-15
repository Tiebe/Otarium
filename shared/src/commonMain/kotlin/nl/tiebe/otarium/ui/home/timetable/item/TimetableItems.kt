package nl.tiebe.otarium.ui.home.timetable.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.tiebe.magisterapi.response.general.year.agenda.AgendaItem
import nl.tiebe.otarium.logic.root.home.children.timetable.children.timetable.TimetableComponent
import nl.tiebe.otarium.logic.root.home.children.timetable.children.timetable.days
import nl.tiebe.otarium.magister.getAgendaForDay
import nl.tiebe.otarium.ui.utils.HtmlView
import nl.tiebe.otarium.ui.utils.rectBorder
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import nl.tiebe.otarium.magister.AgendaItemWithAbsence
import nl.tiebe.otarium.magister.arrangeEvents
import kotlin.math.floor
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
internal fun TimetableItems(
    component: TimetableComponent,
    page: Int,
    timesShown: IntRange,
    dpPerHour: Dp,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val pageWeek = if (page >= 0) {
            page / days.size
        } else {
            floor((page / days.size.toFloat())).toInt()
        }

        val startOfWeekDate = component.now.value.date.minus(
            component.now.value.date.dayOfWeek.ordinal,
            DateTimeUnit.DAY
        ) // first day of week
            .plus(pageWeek * 7, DateTimeUnit.DAY) // add weeks to get to selected week
            .plus(
                page - (pageWeek * days.size),
                DateTimeUnit.DAY
            ) // add days to get to selected day

        val timeTop: Long = startOfWeekDate.atStartOfDayIn(TimeZone.of("Europe/Amsterdam"))
            .toEpochMilliseconds() + (timesShown.first() * 60 * 60 * 1000)

        val timetable = component.timetable.subscribeAsState()
        val events = component.getTimetableForWeek(timetable.value, startOfWeekDate).getAgendaForDay(page - (pageWeek * days.size))

        val items = remember(events) { arrangeEvents(events.sortedBy { it.start }) }
        Layout(
            content = {
                items.forEach { item ->
                    Box(modifier = Modifier.eventData(item)) {
                        TimetableItem(Modifier, item) { component.openTimeTableItem(item) } //todo: onclick
                    }
                }
            },
            modifier = Modifier.padding(start = 40.5.dp)
        ) { measureables, constraints ->
            val height = (dpPerHour.toPx() * (timesShown.last - timesShown.first + 1)).roundToInt()
            val width = constraints.maxWidth
            val placeablesWithEvents = measureables.map { measurable ->
                val splitEvent = measurable.parentData as AgendaItemWithAbsence
                val eventDurationMinutes = (splitEvent.end - splitEvent.start).inWholeMinutes
                val eventHeight = ((eventDurationMinutes / 60f) * dpPerHour.toPx()).roundToInt()
                val eventWidth = ((splitEvent.colSpan.toFloat() / splitEvent.colTotal.toFloat()) * width).roundToInt()
                val placeable = measurable.measure(constraints.copy(minWidth = eventWidth, maxWidth = eventWidth, minHeight = eventHeight, maxHeight = eventHeight))
                Pair(placeable, splitEvent)
            }
            layout(width, height) {
                placeablesWithEvents.forEach { (placeable, splitEvent) ->
                    val eventOffsetMinutes = (splitEvent.start.toEpochMilliseconds() - timeTop)/ 1000 / 60
                    val eventY = ((eventOffsetMinutes / 60f) * dpPerHour.toPx()).roundToInt()
                    val eventX = (splitEvent.col * (width / splitEvent.colTotal.toFloat())).roundToInt()
                    placeable.place(eventX, eventY)
                }
            }
        }
    }
}

@Composable
fun TimetableItem(modifier: Modifier, agendaItemWithAbsence: AgendaItemWithAbsence, onClick: () -> Unit) {
    val agendaItem = agendaItemWithAbsence.agendaItem
    val absence = agendaItemWithAbsence.absence
    val startTime =
        agendaItem.start.substring(0, 26).toLocalDateTime().toInstant(TimeZone.UTC)
    val endTime =
        agendaItem.einde.substring(0, 26).toLocalDateTime().toInstant(TimeZone.UTC)

    val localStartTime = startTime.toLocalDateTime(TimeZone.of("Europe/Amsterdam"))
    val localEndTime = endTime.toLocalDateTime(TimeZone.of("Europe/Amsterdam"))

    val overlineContent = mutableListOf<String>()

    if (!agendaItem.location.isNullOrEmpty()) overlineContent.add(agendaItem.location!!)
    overlineContent.add(
        "${
            localStartTime.hour.toString().padStart(2, '0')
        }:${localStartTime.minute.toString().padStart(2, '0')} - ${
            localEndTime.hour.toString().padStart(2, '0')
        }:${localEndTime.minute.toString().padStart(2, '0')}"
    )

    if (!agendaItem.content.isNullOrEmpty()) overlineContent.add(
        agendaItem.content!!
    )

    ListItem(
        modifier = Modifier
            .fillMaxHeight()
            .rectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline), drawLeft = agendaItemWithAbsence.col != 0, drawRight = agendaItemWithAbsence.col + agendaItemWithAbsence.colSpan != agendaItemWithAbsence.colTotal)
            .clickable { onClick() }
            .then(modifier),
        headlineContent = { Text(agendaItem.description ?: "") },
        supportingContent = {
            HtmlView(
                overlineContent.joinToString(" • "),
                maxLines = 1,
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
            containerColor = if (
                agendaItem.getStatus() == AgendaItem.Companion.Status.CANCELED_AUTOMATICALLY
                || agendaItem.getStatus() == AgendaItem.Companion.Status.CANCELED_MANUALLY
            ) {
                MaterialTheme.colorScheme.errorContainer
            } else {
                MaterialTheme.colorScheme.inverseOnSurface
            },
        ),
    )
}

private fun Modifier.eventData(agendaItemWithAbsence: AgendaItemWithAbsence) = this.then(EventDataModifier(agendaItemWithAbsence))

private class EventDataModifier(
    val agendaItemWithAbsence: AgendaItemWithAbsence,
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = agendaItemWithAbsence
}

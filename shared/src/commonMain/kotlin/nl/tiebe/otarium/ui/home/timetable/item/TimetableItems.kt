package nl.tiebe.otarium.ui.home.timetable.item

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import dev.tiebe.magisterapi.response.general.year.absence.Absence
import dev.tiebe.magisterapi.response.general.year.agenda.AgendaItem
import kotlinx.datetime.*
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.logic.root.home.children.timetable.TimetableRootComponent
import nl.tiebe.otarium.logic.root.home.children.timetable.children.timetable.TimetableComponent
import nl.tiebe.otarium.magister.AgendaItemWithAbsence
import nl.tiebe.otarium.magister.arrangeEvents
import nl.tiebe.otarium.magister.getAgendaForDay
import nl.tiebe.otarium.ui.utils.rectBorder
import kotlin.math.floor
import kotlin.math.roundToInt

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
            page / component.days.size
        } else {
            floor((page / component.days.size.toFloat())).toInt()
        }

        val startOfWeekDate = component.now.value.date.minus(
            component.now.value.date.dayOfWeek.ordinal,
            DateTimeUnit.DAY
        ) // first day of week
            .plus(pageWeek * 7, DateTimeUnit.DAY) // add weeks to get to selected week
            .plus(
                page - (pageWeek * component.days.size),
                DateTimeUnit.DAY
            ) // add days to get to selected day

        val timeTop: Long = startOfWeekDate.atStartOfDayIn(TimeZone.of("Europe/Amsterdam"))
            .toEpochMilliseconds() + (timesShown.first() * 60 * 60 * 1000)

        val timetable = component.timetable.subscribeAsState()
        val events = component.getTimetableForWeek(timetable.value, startOfWeekDate).getAgendaForDay(page - (pageWeek * component.days.size))

        val items = remember(events) { arrangeEvents(events.sortedBy { it.start }) }
        Layout(
            content = {
                items.forEach { item ->
                    Box(modifier = Modifier.eventData(item)) {
                        TimetableItem(Modifier, item) { component.parentComponent.navigate(TimetableRootComponent.Config.TimetablePopup(item)) }
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

    val containerColor = if (
        agendaItem.getStatus() == AgendaItem.Companion.Status.CANCELED_AUTOMATICALLY
        || agendaItem.getStatus() == AgendaItem.Companion.Status.CANCELED_MANUALLY
    ) {
        MaterialTheme.colorScheme.errorContainer
    } else {
        if (Data.timetableContrast) MaterialTheme.colorScheme.inverseOnSurface
        else MaterialTheme.colorScheme.surface
    }

    var listItemModifier = Modifier
        .fillMaxHeight()
        .clickable { onClick() }

    listItemModifier = if (Data.timetableRounding > 0) {
        listItemModifier.clip(shape = RoundedCornerShape(Data.timetableRounding)).border(brush = SolidColor(MaterialTheme.colorScheme.outline), shape = RoundedCornerShape(Data.timetableRounding), width = Dp.Hairline)
    } else {
        listItemModifier.rectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline), drawLeft = agendaItemWithAbsence.col != 0, drawRight = agendaItemWithAbsence.col + agendaItemWithAbsence.colSpan != agendaItemWithAbsence.colTotal)
    }

    ListItem(
        modifier = listItemModifier.then(modifier),
        headlineContent = { Text(agendaItem.description ?: "", maxLines = 1, overflow = TextOverflow.Ellipsis) },
        supportingContent = {

            val state = rememberRichTextState()

            LaunchedEffect(overlineContent) {
                state.setHtml(overlineContent.joinToString(" • "))
            }

            RichText(
                state,
                maxLines = 1,
                modifier = Modifier.clickable(onClick = onClick)
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
            AbsenceBox(absence)
        },
        colors = ListItemDefaults.colors(
            containerColor = containerColor,
        ),
    )
}

private fun Modifier.eventData(agendaItemWithAbsence: AgendaItemWithAbsence) = this.then(EventDataModifier(agendaItemWithAbsence))

private class EventDataModifier(
    val agendaItemWithAbsence: AgendaItemWithAbsence,
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = agendaItemWithAbsence
}

@Composable
fun AbsenceBox(absence: Absence?) {
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
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.bodySmall,
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
                color = MaterialTheme.colorScheme.onTertiary,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
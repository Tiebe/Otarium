package dev.tiebe.otarium.ui.home.timetable.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.tiebe.magisterapi.response.general.year.agenda.AgendaItem
import dev.tiebe.otarium.logic.root.home.children.timetable.children.timetable.TimetableComponent
import dev.tiebe.otarium.logic.root.home.children.timetable.children.timetable.days
import dev.tiebe.otarium.magister.getAgendaForDay
import dev.tiebe.otarium.ui.utils.HtmlView
import dev.tiebe.otarium.ui.utils.topBottomRectBorder
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.math.floor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TimetableItem(
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

        component.getTimetableForWeek(timetable.value, startOfWeekDate).getAgendaForDay(page - (pageWeek * days.size))
            .forEach { agendaItemWithAbsence ->
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

                val supportingText = mutableListOf<String>()

                if (!agendaItem.location.isNullOrEmpty()) supportingText.add(agendaItem.location!!)
                supportingText.add(
                    "${
                        localStartTime.hour.toString().padStart(2, '0')
                    }:${localStartTime.minute.toString().padStart(2, '0')} - ${
                        localEndTime.hour.toString().padStart(2, '0')
                    }:${localEndTime.minute.toString().padStart(2, '0')}"
                )

                if (!agendaItem.content.isNullOrEmpty()) supportingText.add(
                    agendaItem.content!!
                )

                ListItem(
                    modifier = Modifier
                        .padding(start = 40.5.dp, top = distanceAfterTop)
                        .height(height)
                        .topBottomRectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline))
                        .clickable { component.openTimeTableItem(agendaItemWithAbsence) },
                    headlineText = { Text(agendaItem.description ?: "") },
                    supportingText = {
                        HtmlView(
                            supportingText.joinToString(" • "),
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
    }
}
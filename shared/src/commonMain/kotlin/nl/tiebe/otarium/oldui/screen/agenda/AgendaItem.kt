package nl.tiebe.otarium.oldui.screen.agenda

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.datetime.*
import nl.tiebe.otarium.magister.AgendaItemWithAbsence
import nl.tiebe.otarium.oldui.utils.topBottomRectBorder
import nl.tiebe.otarium.utils.ui.parseHtml
import kotlin.math.floor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AgendaItem(
    currentPage: Int,
    totalDays: Int,
    now: LocalDateTime,
    timesShown: IntRange,
    dpPerHour: Dp,
    loadedAgendas: MutableMap<Int, List<List<AgendaItemWithAbsence>>>,
    openAgendaItemWithAbsence: (AgendaItemWithAbsence) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val dayOfWeek = (currentPage.mod(totalDays))

        val pageWeek = if (currentPage >= 0) {
            currentPage / totalDays
        } else {
            floor((currentPage / totalDays.toFloat())).toInt()
        }

        val dayOfWeekStartMillis = now.date.minus(
            now.date.dayOfWeek.ordinal,
            DateTimeUnit.DAY
        ) // first day of week
            .plus(pageWeek * 7, DateTimeUnit.DAY) // add weeks to get to selected week
            .plus(
                currentPage - (pageWeek * totalDays),
                DateTimeUnit.DAY
            ) // add days to get to selected day
            .atStartOfDayIn(TimeZone.of("Europe/Amsterdam")).toEpochMilliseconds()

        val timeTop: Long = dayOfWeekStartMillis + (timesShown.first() * 60 * 60 * 1000)

        loadedAgendas[pageWeek + 100]?.getOrNull(dayOfWeek)?.forEach { agendaItemWithAbsence ->
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
            supportingText.add(AnnotatedString(
                "${
                    localStartTime.hour.toString().padStart(2, '0')
                }:${localStartTime.minute.toString().padStart(2, '0')} - ${
                    localEndTime.hour.toString().padStart(2, '0')
                }:${localEndTime.minute.toString().padStart(2, '0')}"
            ))

            if (!agendaItem.content.isNullOrEmpty()) supportingText.add(
                agendaItem.content!!.parseHtml()
            )

            ListItem(
                modifier = Modifier
                    .padding(start = 40.5.dp, top = distanceAfterTop)
                    .height(height)
                    .topBottomRectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline))
                    .clickable { openAgendaItemWithAbsence(agendaItemWithAbsence) },
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
    }
}
package nl.tiebe.otarium.android.ui.screen.agenda

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import kotlinx.datetime.*
import nl.tiebe.magisterapi.response.general.year.agenda.AgendaItem
import nl.tiebe.otarium.android.ui.utils.topBottomRectBorder
import kotlin.math.floor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaItem(
    dayScrollState: ScrollState,
    currentPage: Int,
    totalDays: Int,
    now: LocalDateTime,
    timesShown: IntRange,
    dpPerHour: Dp,
    loadedAgendas: MutableMap<Int, List<List<AgendaItem>>>
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 40.5.dp)
            .verticalScroll(dayScrollState)
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

        for (item in loadedAgendas[pageWeek]?.get(dayOfWeek) ?: emptyList()) {
            val startTime = item.start.substring(0, 26).toLocalDateTime()
            val endTime = item.einde.substring(0, 26).toLocalDateTime()

            val height =
                dpPerHour * ((endTime.toInstant(TimeZone.UTC)
                    .toEpochMilliseconds() - startTime.toInstant(
                    TimeZone.UTC
                ).toEpochMilliseconds()).toFloat() / 60 / 60 / 1000)
            var distanceAfterTop =
                (dpPerHour * ((startTime.toInstant(TimeZone.UTC)
                    .toEpochMilliseconds() - timeTop).toFloat() / 60 / 60 / 1000))
            if (distanceAfterTop < 0.dp) distanceAfterTop = 0.dp

            val supportingText = mutableListOf<String>()

            if (!item.location.isNullOrEmpty()) supportingText.add(item.location!!)
            supportingText.add(
                "${
                    startTime.hour.toString().padStart(2, '0')
                }:${startTime.minute.toString().padStart(2, '0')} - ${
                    endTime.hour.toString().padStart(2, '0')
                }:${endTime.minute.toString().padStart(2, '0')}"
            )

            if (!item.content.isNullOrEmpty()) supportingText.add(
                HtmlCompat.fromHtml(
                    item.content!!,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                ).toString()
            )

            ListItem(
                modifier = Modifier
                    .padding(top = distanceAfterTop)
                    .height(height)
                    .topBottomRectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline)),
                headlineText = { Text(item.description ?: "") },
                supportingText = {
                    Text(
                        supportingText.joinToString(" • "),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                leadingContent = {
                    if (item.fromPeriod != null) {
                        Text(item.fromPeriod!!.toString(), modifier = Modifier.padding(2.dp))
                    } else {
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface
                ),
            )
        }
    }
}


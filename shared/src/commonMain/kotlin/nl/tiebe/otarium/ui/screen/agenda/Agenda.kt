package nl.tiebe.otarium.ui.screen.agenda

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import nl.tiebe.otarium.magister.AgendaItemWithAbsence
import nl.tiebe.otarium.ui.utils.topRectBorder

val timesShown = 8..17
val dpPerHour = 80.dp // yes that .14 is needed. don't ask me how i found out

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Agenda(
    initialPage: Int,
    dayPagerState: PagerState,
    days: List<String>,
    loadedAgendas: MutableMap<Int, List<List<AgendaItemWithAbsence>>>,
    refresh: (refreshState: SwipeRefreshState) -> Unit,
    openAgendaItemWithAbsence: (AgendaItemWithAbsence) -> Unit
) {
    val refreshState = rememberSwipeRefreshState(false)

    var now = remember { Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam")) }

    val timeLinePosition = remember {
        mutableStateOf(0.dp)
    }

    val scrollState = rememberScrollState()

    HorizontalPager(
        count = 1000,
        state = dayPagerState,
    ) { page ->
        SwipeRefresh(
            state = refreshState,
            onRefresh = { refresh(refreshState) }
        ) {
            Box(Modifier.verticalScroll(scrollState)) {
                if (timeLinePosition.value > 0.dp && page == initialPage) {
                    Divider(
                        Modifier
                            .width(40.dp)
                            .padding(top = timeLinePosition.value),
                        color = MaterialTheme.colorScheme.primary
                    )
                }


                Column {
                    for (i in timesShown) {
                        Text(
                            text = i.toString(),
                            Modifier
                                .size(40.dp, dpPerHour)
                                .topRectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline))
                                .padding(8.dp),
                            MaterialTheme.colorScheme.outline
                        )
                    }
                }

                AgendaItem(
                    currentPage = page - (dayPagerState.pageCount / 2),
                    totalDays = days.size,
                    now = now,
                    timesShown = timesShown,
                    dpPerHour = dpPerHour,
                    loadedAgendas = loadedAgendas,
                    openAgendaItemWithAbsence = openAgendaItemWithAbsence
                )
            }
        }
    }

    DisposableEffect(Unit) {
        runBlocking {
            launch {
                now = Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam"))

                val minutes = ((now.hour - 8) * 60) + now.minute

                if (minutes < 0) {
                    timeLinePosition.value = 0.dp
                } else {
                    timeLinePosition.value = minutes / 60f * dpPerHour
                }

                delay(60_000)
            }
        }

        onDispose {}
    }
}
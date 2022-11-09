package nl.tiebe.otarium.android.ui.screen.agenda

import android.os.Handler
import android.os.Looper
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
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import nl.tiebe.magisterapi.response.general.year.agenda.AgendaItem
import nl.tiebe.otarium.android.ui.utils.topRectBorder

val timesShown = 8..17
val dpPerHour = 73.14.dp // yes that .14 is needed. don't ask me how i found out

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Agenda(dayPagerState: PagerState, days: List<String>, loadedAgendas: MutableMap<Int, List<List<AgendaItem>>>, refresh: (refreshState: SwipeRefreshState) -> Unit) {
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
            if (timeLinePosition.value > 0.dp && page == 500) {
                Box(Modifier.verticalScroll(scrollState)) {
                    Divider(
                        Modifier
                            .width(40.dp)
                            .padding(top = timeLinePosition.value),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
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
                dayScrollState = scrollState,
                currentPage = page-(dayPagerState.pageCount/2),
                totalDays = days.size,
                now = now,
                timesShown = timesShown,
                dpPerHour = dpPerHour,
                loadedAgendas = loadedAgendas
            )
        }
    }

    DisposableEffect(Unit) {
        val handler = Handler(Looper.getMainLooper())

        val runnable = object : Runnable {
            override fun run() {
                now = Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam"))

                val minutes = ((now.hour-8) * 60) + now.minute

                if (minutes < 0) {
                    timeLinePosition.value = 0.dp
                } else {
                    timeLinePosition.value = minutes / 60f * dpPerHour
                }

                handler.postDelayed(this, 60_000)
            }
        }

        handler.post(runnable)

        onDispose {
            handler.removeCallbacks(runnable)
        }
    }
}
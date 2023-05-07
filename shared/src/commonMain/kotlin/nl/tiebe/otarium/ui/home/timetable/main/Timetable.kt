package nl.tiebe.otarium.ui.home.timetable.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.ui.home.timetable.TimetableComponent
import nl.tiebe.otarium.ui.home.timetable.item.TimetableItem

val timesShown = 8..17
val dpPerHour = 80.dp

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
internal fun Timetable(
    component: TimetableComponent,
    dayPagerState: PagerState,
    pageCount: Int
) {
    HorizontalPager(
        pageCount = pageCount,
        state = dayPagerState,
        beyondBoundsPageCount = 3
    ) { page ->
        val refreshState = rememberPullRefreshState(component.isRefreshingTimetable.subscribeAsState().value, { component.refreshSelectedWeek() })
        val scrollState = rememberScrollState()

        Box(Modifier.pullRefresh(refreshState).verticalScroll(scrollState)) {
            val now = component.now.subscribeAsState().value

            val minutes = ((now.hour - timesShown.first) * 60) + now.minute

            if (
                minutes > 0 &&
                page == (component.amountOfDays / 2) + component.now.value.dayOfWeek.ordinal &&
                now.hour in timesShown
            ) {
                Divider(
                    Modifier
                        .width(40.dp)
                        .padding(top = minutes / 60f * dpPerHour),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            TimetableBackground()

            TimetableItem(
                component = component,
                page = page - (pageCount / 2),
                timesShown = timesShown,
                dpPerHour = dpPerHour,
            )

            PullRefreshIndicator(
                refreshing = component.isRefreshingTimetable.subscribeAsState().value,
                state = refreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}
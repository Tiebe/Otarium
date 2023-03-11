package nl.tiebe.otarium.ui.home.timetable.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import nl.tiebe.otarium.oldui.utils.topRectBorder
import nl.tiebe.otarium.ui.home.timetable.TimetableComponent
import nl.tiebe.otarium.ui.home.timetable.item.TimetableItem

val timesShown = 8..17
val dpPerHour = 80.dp

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun Timetable(
    component: TimetableComponent,
    dayPagerState: PagerState,
) {


    HorizontalPager(
        count = 1000,
        state = dayPagerState,
    ) { page ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = component.isRefreshingTimetable.subscribeAsState().value),
            onRefresh = { component.refreshSelectedWeek() }
        ) {
            val scrollState = rememberScrollState()

            Box(Modifier.verticalScroll(scrollState)) {
                val now = component.now.subscribeAsState().value

                val minutes = ((now.hour - 8) * 60) + now.minute

                if (minutes > 0 && page == component.amountOfDays / 2 ) {
                    Divider(
                        Modifier
                            .width(40.dp)
                            .padding(top = minutes / 60f * dpPerHour),
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

                TimetableItem(
                    component = component,
                    currentPage = page - (dayPagerState.pageCount / 2),
                    timesShown = timesShown,
                    dpPerHour = dpPerHour,
                )
            }
        }
    }
}
package nl.tiebe.otarium.ui.home.timetable.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.logic.root.home.children.timetable.children.timetable.TimetableComponent
import nl.tiebe.otarium.logic.root.home.children.timetable.children.timetable.days
import nl.tiebe.otarium.ui.utils.tabIndicatorOffset
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun DaySelector(
    component: TimetableComponent,
    dayPagerState: PagerState,
    weekPagerState: PagerState,
    dayPageCount: Int
) {
    val selectedDay = component.selectedDay.subscribeAsState()

    HorizontalPager(state = weekPagerState) { week ->
        TabRow(
            selectedTabIndex = selectedDay.value,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(Modifier.tabIndicatorOffset(
                        dayPagerState,
                        dayPageCount,
                        tabPositions,
                        shouldShowIndicator = week == component.selectedWeekIndex.subscribeAsState().value
                    ))
            }) {

            days.forEachIndexed { index, title ->
                DayTabItem(component, index, week, dayPagerState, title)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DayTabItem(
    component: TimetableComponent,
    dayIndex: Int,
    weekIndex: Int,
    dayPagerState: PagerState,
    title: String
) {
    val scope = rememberCoroutineScope()

    val selectedDay = component.selectedDay.subscribeAsState()
    val selectedWeekIndex = component.selectedWeekIndex.subscribeAsState()

    Tab(
        selected = selectedDay.value == dayIndex && selectedWeekIndex.value == weekIndex,
        onClick = {
            scope.launch {
                dayPagerState.animateScrollToPage((weekIndex - 100) * days.size + dayIndex + (component.amountOfDays / 2))
            }
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    textAlign = TextAlign.Center,
                    fontSize = 13.sp
                )
                Text(
                    text = component.firstDayOfWeek.plus(
                        (weekIndex - 100) * 7 + dayIndex,
                        DateTimeUnit.DAY
                    ).toString()
                        .split("-").reversed().subList(0, 1).joinToString(),
                    textAlign = TextAlign.Center,
                    fontSize = 10.sp
                )
            }

        }
    )
}
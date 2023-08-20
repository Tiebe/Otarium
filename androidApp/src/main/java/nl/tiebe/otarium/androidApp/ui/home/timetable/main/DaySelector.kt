package nl.tiebe.otarium.androidApp.ui.home.timetable.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import nl.tiebe.otarium.androidApp.ui.utils.tabIndicatorOffset
import nl.tiebe.otarium.logic.root.home.children.timetable.children.timetable.days
import kotlin.math.floor

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun DaySelector(
    dayPagerState: PagerState,
    weekPagerState: PagerState,
) {
    HorizontalPager(state = weekPagerState) { week ->
        TabRow(
            selectedTabIndex = (dayPagerState.currentPage - dayPagerState.pageCount) % days.size,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(Modifier.tabIndicatorOffset(
                        dayPagerState,
                        tabPositions,
                        shouldShowIndicator = week == floor(dayPagerState.currentPage / days.size.toDouble()).toInt()
                    ))
            }) {

            days.forEachIndexed { dayIndex, title ->
                val index = dayIndex + (week * days.size)
                DayTabItem(
                    index == dayPagerState.currentPage &&
                            week == floor(dayPagerState.currentPage / days.size.toDouble()).toInt(),
                    index,
                    dayPagerState,
                    title.name /* todo: localized string */
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DayTabItem(
    selected: Boolean,
    dayIndex: Int,
    dayPagerState: PagerState,
    title: String
) {
    val scope = rememberCoroutineScope()
    val currentDate = remember {
        Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam"))
    }

    val firstDayOfWeek = remember {
        currentDate.date.minus(currentDate.dayOfWeek.ordinal, DateTimeUnit.DAY)
    }

    Tab(
        selected = selected,
        onClick = {
            scope.launch {
                dayPagerState.animateScrollToPage(dayIndex)
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
                    text = firstDayOfWeek.plus(dayIndex - dayPagerState.pageCount / 2, DateTimeUnit.DAY).toString()
                        .split("-").reversed().subList(0, 1).joinToString(),
                    textAlign = TextAlign.Center,
                    fontSize = 10.sp
                )
            }

        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun DaySelectorPreview() {
    val currentDate = remember {
        Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam"))
    }

    DaySelector(
        dayPagerState = rememberPagerState(350 + currentDate.dayOfWeek.ordinal) { 700 },
        weekPagerState = rememberPagerState(50) { 100 }
    )
}
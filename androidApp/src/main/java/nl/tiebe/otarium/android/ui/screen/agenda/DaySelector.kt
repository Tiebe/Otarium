package nl.tiebe.otarium.android.ui.screen.agenda

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import nl.tiebe.otarium.android.ui.utils.pagerTabIndicatorOffset

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DaySelector(dayPagerState: PagerState, days: List<String>, selectedWeek: State<Int>, firstDayOfWeek: LocalDate) {
    val scope = rememberCoroutineScope()
    val weekPagerState = rememberPagerState(100)

    HorizontalPager(count = 200, state = weekPagerState) { week ->
        TabRow(selectedTabIndex = dayPagerState.currentPage, indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(
                    week - 100,
                    selectedWeek,
                    dayPagerState,
                    tabPositions
                )
            )
        }) {
            days.forEachIndexed { index, title ->
                Tab(
                    selected = dayPagerState.currentPage == index && selectedWeek.value == week,
                    onClick = {
                        scope.launch {
                            dayPagerState.animateScrollToPage(week * days.size + index)
                        }
                    },
                    text = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = title,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = firstDayOfWeek.plus(
                                    (week - 100) * 7 + index,
                                    DateTimeUnit.DAY
                                ).toString()
                                    .split("-").reversed().subList(0, 2).joinToString("-"),
                                textAlign = TextAlign.Center
                            )
                        }

                    }
                )
            }
        }

        // update selected week when swiping days
        LaunchedEffect(dayPagerState) {
            snapshotFlow { dayPagerState.currentPage }.collect { page ->
                if (selectedWeek.value != (((page) - (dayPagerState.pageCount / 2)) / days.size)) {
                    scope.launch {
                        println("FUcky ou")
                        weekPagerState.animateScrollToPage(selectedWeek.value + 100)
                    }
                }

            }
        }
    }
}
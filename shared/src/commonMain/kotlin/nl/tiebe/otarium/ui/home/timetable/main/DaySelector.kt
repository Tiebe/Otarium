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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import nl.tiebe.otarium.ui.utils.tabIndicatorOffset

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun DaySelector(
    component: TimetableComponent,
    dayPagerState: PagerState,
    weekPagerState: PagerState,
    dayPageCount: Int,
    weekPageCount: Int
) {
    val scope = rememberCoroutineScope()
    val selectedWeek = component.selectedWeek.subscribeAsState()

    HorizontalPager(pageCount = weekPageCount, state = weekPagerState) { week ->
        TabRow(
            selectedTabIndex = (dayPagerState.currentPage - (dayPageCount / 2)) % 7,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(
                        dayPagerState,
                        dayPageCount,
                        tabPositions,
                        shouldShowIndicator = derivedStateOf { week == weekPageCount/2 + selectedWeek.value }.value
                    )
                )
            }) {
            days.forEachIndexed { index, title ->
                Tab(
                    selected = (dayPagerState.currentPage - (dayPageCount / 2)) % 7 == index && week == 100 + component.selectedWeek.subscribeAsState().value,
                    onClick = {
                        scope.launch {
                            dayPagerState.animateScrollToPage((week-100)* days.size + index + (component.amountOfDays / 2))
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
                                    (week - 100) * 7 + index,
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
        }
    }
}
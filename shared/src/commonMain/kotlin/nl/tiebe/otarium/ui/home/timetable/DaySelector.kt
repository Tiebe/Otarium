package nl.tiebe.otarium.ui.home.timetable

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import nl.tiebe.otarium.oldui.utils.pagerTabIndicatorOffset

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun DaySelector(
    component: TimetableComponent,
    dayPagerState: PagerState,
) {
    val scope = rememberCoroutineScope()
    val weekPagerState = rememberPagerState(100)

    HorizontalPager(count = 200, state = weekPagerState) { week ->
        TabRow(
            selectedTabIndex = dayPagerState.currentPage - (dayPagerState.pageCount / 2),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(
                        week - 100,
                        component.selectedWeek.subscribeAsState().value,
                        dayPagerState,
                        tabPositions
                    )
                )
            }) {
            component.days.forEachIndexed { index, title ->
                Tab(
                    selected = dayPagerState.currentPage == index && component.selectedWeek.subscribeAsState().value == week,
                    onClick = {
                        scope.launch {
                            dayPagerState.animateScrollToPage((week-100)*component.days.size + index + (component.amountOfDays / 2))
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
                                    component.selectedWeek.value * 7,
                                    DateTimeUnit.DAY
                                ).plus(
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
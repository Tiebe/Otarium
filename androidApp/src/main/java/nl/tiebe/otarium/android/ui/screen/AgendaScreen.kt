package nl.tiebe.otarium.android.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import nl.tiebe.magisterapi.response.general.year.agenda.AgendaItem
import nl.tiebe.otarium.android.R
import nl.tiebe.otarium.android.ui.utils.pagerTabIndicatorOffset
import nl.tiebe.otarium.magister.Tokens
import nl.tiebe.otarium.magister.getAgendaForDay
import nl.tiebe.otarium.magister.getMagisterAgenda
import nl.tiebe.otarium.magister.getSavedAgenda
import nl.tiebe.otarium.utils.server.getMagisterTokens
import kotlin.math.floor

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Preview(showBackground = true)
@Composable
fun AgendaScreen() {
    // TODO: Retrieve new agenda
    // TODO: Dont just load the same agenda again and again, load different weeks

    val scope = rememberCoroutineScope()
    val dayPagerState = rememberPagerState(500)
    val weekPagerState = rememberPagerState(100)

    val refreshState = rememberSwipeRefreshState(false)

    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val firstDayOfWeek = now.date.minus(now.date.dayOfWeek.ordinal, DateTimeUnit.DAY)

    var agenda = getSavedAgenda()
    val selectedWeekAgenda: MutableList<List<AgendaItem>> = mutableListOf()

    val titles = listOf(stringResource(R.string.mon), stringResource(R.string.tue), stringResource(R.string.wed), stringResource(R.string.thu), stringResource(R.string.fri))
    titles.indices.forEach { selectedWeekAgenda.add(it, agenda.getAgendaForDay(it)) }

    // some math to get the selected week from the currently selected day
    val selectedWeek = remember { derivedStateOf{
        if (dayPagerState.currentPage-(dayPagerState.pageCount/2) >= 0) {
            ((dayPagerState.currentPage)-(dayPagerState.pageCount/2))/titles.size
        } else {
            floor(((dayPagerState.currentPage) - (dayPagerState.pageCount / 2))/titles.size.toFloat()).toInt()
        }
    } }

    val firstDayOfSelectedWeek = remember { derivedStateOf {
        firstDayOfWeek.plus(selectedWeek.value*7, DateTimeUnit.DAY)
    } }

    LaunchedEffect(selectedWeek.value) {
        println("Selected week: ${selectedWeek.value}")

        getMagisterTokens(Tokens.getPastTokens()?.accessTokens?.accessToken)?.let { tokens ->
            val start = firstDayOfSelectedWeek.value
            val end = start.plus(titles.size, DateTimeUnit.DAY)

            println("$start - $end")

            println("getting agenda")
            agenda = getMagisterAgenda(
                tokens.accountId,
                tokens.tenantUrl,
                tokens.tokens.accessToken,
                start,
                end
            )
            println("got agenda")

            selectedWeekAgenda.removeAll { true }
            titles.indices.forEach { selectedWeekAgenda.add(it, agenda.getAgendaForDay(it)) }
        }
    }

    // update selected week when swiping days
    LaunchedEffect(dayPagerState) {
        snapshotFlow { dayPagerState.currentPage }.collect { page ->
            if (weekPagerState.currentPage != (((page)-(dayPagerState.pageCount/2))/5)) {
                scope.launch {
                    weekPagerState.animateScrollToPage(selectedWeek.value+100)
                }
            }

        }
    }


    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(count = 200, state = weekPagerState) { week ->
            TabRow(selectedTabIndex = dayPagerState.currentPage, indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(
                        week-100,
                        selectedWeek,
                        dayPagerState,
                        tabPositions
                    )
                )
            }) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        selected = dayPagerState.currentPage == index && selectedWeek.value == week,
                        onClick = {
                            scope.launch {
                                dayPagerState.animateScrollToPage(week*titles.size+index)
                            } },
                        text = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = title,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = firstDayOfWeek.plus((week-100)*7+index, DateTimeUnit.DAY).toString()
                                        .split("-").reversed().subList(0, 2).joinToString("-"),
                                    textAlign = TextAlign.Center
                                )
                            }

                        }
                    )
                }
            }
        }
        HorizontalPager(
            count = 1000,
            state = dayPagerState,
        ) { tabIndex ->
            SwipeRefresh(
                state = refreshState,
                onRefresh = {
                    println("refreshing")
                    scope.launch {
                    refreshState.isRefreshing = true
                    println("getting tokens")
                    getMagisterTokens(Tokens.getPastTokens()?.accessTokens?.accessToken)?.let { tokens ->
                        val start = firstDayOfSelectedWeek.value
                        val end = start.plus(titles.size, DateTimeUnit.DAY)

                        println("$start - $end")

                        println("getting agenda")
                        agenda = getMagisterAgenda(
                            tokens.accountId,
                            tokens.tenantUrl,
                            tokens.tokens.accessToken,
                            start,
                            end
                        )

                        selectedWeekAgenda.removeAll { true }
                        titles.indices.forEach { selectedWeekAgenda.add(it, agenda.getAgendaForDay(it)) }
                    }
                    refreshState.isRefreshing = false
                }}
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    val dayOfWeek = ((tabIndex - 500).mod(titles.size))
                    for (item in selectedWeekAgenda[dayOfWeek]) {
                        ListItem(
                            headlineText = { Text(item.description ?: "") },
                        )
                        Divider()
                    }
                }
            }
        }
    }
}
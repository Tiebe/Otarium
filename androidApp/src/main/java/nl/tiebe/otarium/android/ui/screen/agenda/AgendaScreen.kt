package nl.tiebe.otarium.android.ui.screen.agenda

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import nl.tiebe.magisterapi.response.general.year.agenda.AgendaItem
import nl.tiebe.magisterapi.utils.MagisterException
import nl.tiebe.otarium.android.R
import nl.tiebe.otarium.android.ui.utils.pagerTabIndicatorOffset
import nl.tiebe.otarium.android.ui.utils.topRectBorder
import nl.tiebe.otarium.magister.*
import nl.tiebe.otarium.utils.server.getMagisterTokens
import kotlin.math.floor

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalPagerApi::class)
@Preview(showBackground = true)
@Composable
fun AgendaScreen() {
    val scope = rememberCoroutineScope()
    val dayPagerState = rememberPagerState(500)
    val weekPagerState = rememberPagerState(100)

    val dayScrollState = rememberScrollState()

    val refreshState = rememberSwipeRefreshState(false)

    val timesShown = 8..17

    val now = Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam"))
    val firstDayOfWeek = now.date.minus(now.date.dayOfWeek.ordinal, DateTimeUnit.DAY)

    val dpPerHour = 73.14.dp // yes that .14 is needed. don't ask me how i found out

    val titles = listOf(
        stringResource(R.string.mon),
        stringResource(R.string.tue),
        stringResource(R.string.wed),
        stringResource(R.string.thu),
        stringResource(R.string.fri)
    )

    val savedAgenda = getSavedAgenda()
    val loadedAgendas = remember {
        mutableStateMapOf(
            Pair(0, titles.indices.map {
                savedAgenda.getAgendaForDay(it)
            })
        )
    }

    // some math to get the selected week from the currently selected day
    val selectedWeek = remember {
        derivedStateOf {
            if (dayPagerState.currentPage - (dayPagerState.pageCount / 2) >= 0) {
                ((dayPagerState.currentPage) - (dayPagerState.pageCount / 2)) / titles.size
            } else {
                floor(((dayPagerState.currentPage) - (dayPagerState.pageCount / 2)) / titles.size.toFloat()).toInt()
            }
        }
    }

    val firstDayOfSelectedWeek = remember {
        derivedStateOf {
            firstDayOfWeek.plus(selectedWeek.value * 7, DateTimeUnit.DAY)
        }
    }

    LaunchedEffect(selectedWeek.value) {
        try {
            getMagisterTokens(Tokens.getPastTokens()?.accessTokens?.accessToken)?.let { tokens ->
                loadedAgendas[selectedWeek.value] = refreshAgenda(firstDayOfSelectedWeek.value, titles.size-1, selectedWeek.value) ?: loadedAgendas[selectedWeek.value] ?: listOf()
            }
        } catch (e: MagisterException) {
            e.printStackTrace()
        } catch (_: Exception) {}
    }

    // update selected week when swiping days
    LaunchedEffect(dayPagerState) {
        snapshotFlow { dayPagerState.currentPage }.collect { page ->
            if (weekPagerState.currentPage != (((page) - (dayPagerState.pageCount / 2)) / 5)) {
                scope.launch {
                    weekPagerState.animateScrollToPage(selectedWeek.value + 100)
                }
            }

        }
    }

    val timeLinePosition = remember {
        mutableStateOf(0.dp)
    }

    DisposableEffect(Unit) {
        val handler = Handler(Looper.getMainLooper())

        val runnable = object : Runnable {
            override fun run() {
                val time = Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam"))
                val minutes = ((time.hour-8) * 60) + time.minute

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


    Column(modifier = Modifier.fillMaxSize()) {
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
                titles.forEachIndexed { index, title ->
                    Tab(
                        selected = dayPagerState.currentPage == index && selectedWeek.value == week,
                        onClick = {
                            scope.launch {
                                dayPagerState.animateScrollToPage(week * titles.size + index)
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
        }

        HorizontalPager(
            count = 1000,
            state = dayPagerState,
        ) { page ->
            SwipeRefresh(
                state = refreshState,
                onRefresh = {
                    scope.launch {
                        refreshState.isRefreshing = true
                        loadedAgendas[selectedWeek.value] = refreshAgenda(firstDayOfSelectedWeek.value, titles.size-1, selectedWeek.value) ?: loadedAgendas[selectedWeek.value] ?: listOf()
                        refreshState.isRefreshing = false
                    }
                }
            ) {
                if (timeLinePosition.value > 0.dp) {
                    Box(Modifier.verticalScroll(dayScrollState)) {
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
                    dayScrollState = dayScrollState,
                    currentPage = page-(dayPagerState.pageCount/2),
                    totalDays = titles.size,
                    now = now,
                    timesShown = timesShown,
                    dpPerHour = dpPerHour,
                    loadedAgendas = loadedAgendas
                )
            }
        }
    }
}

suspend fun refreshAgenda(
    start: LocalDate,
    totalDays: Int,
    selectedWeek: Int
): List<List<AgendaItem>>? {
    getMagisterTokens(Tokens.getPastTokens()?.accessTokens?.accessToken)?.let { tokens ->
        val end = start.plus(totalDays, DateTimeUnit.DAY)

        val agenda = getMagisterAgenda(
            tokens.accountId,
            tokens.tenantUrl,
            tokens.tokens.accessToken,
            start,
            end
        )

        if (selectedWeek == 0) {
            saveAgenda(agenda)
        }

        return (0..totalDays).map { agenda.getAgendaForDay(it) }
    }
    return null
}
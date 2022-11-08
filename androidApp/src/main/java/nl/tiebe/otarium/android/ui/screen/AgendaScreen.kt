package nl.tiebe.otarium.android.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import kotlinx.datetime.*
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
    // TODO: show refreshing thing when refreshing
    // TODO: show line at current time
    // TODO: make other weeks than the current work

    val scope = rememberCoroutineScope()
    val dayPagerState = rememberPagerState(500)
    val weekPagerState = rememberPagerState(100)

    val dayScrollState = rememberScrollState()

    val refreshState = rememberSwipeRefreshState(false)

    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val firstDayOfWeek = now.date.minus(now.date.dayOfWeek.ordinal, DateTimeUnit.DAY)

    val titles = listOf(stringResource(R.string.mon), stringResource(R.string.tue), stringResource(R.string.wed), stringResource(R.string.thu), stringResource(R.string.fri))

    var agenda = getSavedAgenda()
    val loadedAgendas = remember { mutableStateMapOf(
        Pair(0, titles.indices.map {
            agenda.getAgendaForDay(it)
        })
    ) }

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
        try {
            println("Selected week: ${selectedWeek.value}")

            getMagisterTokens(Tokens.getPastTokens()?.accessTokens?.accessToken)?.let { tokens ->
                val start = firstDayOfSelectedWeek.value
                val end = start.plus(titles.size-1, DateTimeUnit.DAY)

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

                loadedAgendas[selectedWeek.value] = titles.indices.map { agenda.getAgendaForDay(it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
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
                    scope.launch {
                        refreshState.isRefreshing = true
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

                            loadedAgendas[selectedWeek.value] = titles.indices.map { agenda.getAgendaForDay(it) }
                        }
                        refreshState.isRefreshing = false
                }}
            ) {
                val timesShown = 8..17
                val dpPerHour = 73.14.dp // yes that .14 is needed. don't ask me how i found out

                Box(Modifier.verticalScroll(dayScrollState)) {
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

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 40.5.dp)
                    ) {
                        val dayOfWeek = ((tabIndex - 500).mod(titles.size))

                        val pageWeek = if (tabIndex-(dayPagerState.pageCount/2) >= 0) {
                            (tabIndex-(dayPagerState.pageCount/2))/titles.size
                        } else {
                            floor((tabIndex - (dayPagerState.pageCount / 2))/titles.size.toFloat()).toInt()
                        }

                        val dayOfWeekStartMillis = now.date.minus(now.date.dayOfWeek.ordinal, DateTimeUnit.DAY) // first day of week
                            .plus(pageWeek*7, DateTimeUnit.DAY).plus(tabIndex - 500, DateTimeUnit.DAY)
                            .atStartOfDayIn(TimeZone.of("Europe/Amsterdam")).toEpochMilliseconds()

                        val timeTop: Long = dayOfWeekStartMillis + (timesShown.first() * 60 * 60 * 1000)

                        for (item in loadedAgendas[pageWeek]?.get(dayOfWeek) ?: emptyList()) {
                            val startTime = item.start.substring(0, 26).toLocalDateTime()
                                .toInstant(TimeZone.UTC)
                            val endTime = item.einde.substring(0, 26).toLocalDateTime()
                                .toInstant(TimeZone.UTC)

                            val height = dpPerHour * ((endTime.toEpochMilliseconds() - startTime.toEpochMilliseconds()).toFloat() / 60 / 60 / 1000)
                            var distanceAfterTop = (dpPerHour * ((startTime.toEpochMilliseconds() - timeTop).toFloat() / 60 / 60 / 1000))
                            if (distanceAfterTop < 0.dp) distanceAfterTop = 0.dp

                            ListItem(
                                modifier = Modifier
                                    .padding(top = distanceAfterTop)
                                    .height(height)
                                    .topBottomRectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline)),
                                headlineText = { Text(item.description ?: "") },
                                colors = ListItemDefaults.colors(
                                    containerColor = MaterialTheme.colorScheme.inverseOnSurface
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Suppress("UnnecessaryComposedModifier")
fun Modifier.topRectBorder(width: Dp = Dp.Hairline, brush: Brush = SolidColor(Color.LightGray)): Modifier = composed(
    factory = {
        this.then(
            Modifier.drawWithCache {
                onDrawWithContent {
                    drawContent()
                    drawLine(brush, Offset(width.value, size.height+width.value), Offset(411.5.dp.toPx(), size.height+width.value))
                    drawLine(brush, Offset(size.width, 0f-width.value), Offset(size.width, size.height-width.value))
                }
            }
        )
    },
    inspectorInfo = debugInspectorInfo {
        name = "border"
        properties["width"] = width
        if (brush is SolidColor) {
            properties["color"] = brush.value
            value = brush.value
        } else {
            properties["brush"] = brush
        }
        properties["shape"] = RectangleShape
    }
)

@Suppress("UnnecessaryComposedModifier")
fun Modifier.topBottomRectBorder(width: Dp = Dp.Hairline, brush: Brush = SolidColor(Color.LightGray)): Modifier = composed(
    factory = {
        this.then(
            Modifier.drawWithCache {
                onDrawWithContent {
                    drawContent()
                    drawLine(brush, Offset(0f, 0f-width.value), Offset(size.width, 0f-width.value))
                    drawLine(brush, Offset(0f, size.height - width.value), Offset(size.width, size.height - width.value))
                }
            }
        )
    },
    inspectorInfo = debugInspectorInfo {
        name = "border"
        properties["width"] = width
        if (brush is SolidColor) {
            properties["color"] = brush.value
            value = brush.value
        } else {
            properties["brush"] = brush
        }
        properties["shape"] = RectangleShape
    }
)

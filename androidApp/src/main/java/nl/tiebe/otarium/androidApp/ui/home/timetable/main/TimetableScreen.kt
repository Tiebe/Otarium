package nl.tiebe.otarium.androidApp.ui.home.timetable.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import nl.tiebe.otarium.logic.root.home.children.timetable.children.timetable.TimetableComponent


@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun TimetableScreen(component: TimetableComponent) {
    var currentTime = remember {
        Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam"))
    }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam"))
            delay(10000)
        }
    }

    val dayPagerState = rememberPagerState(350 + currentTime.dayOfWeek.ordinal) { 700 }
    val weekPagerState = rememberPagerState(50) { 100 }

    Column(modifier = Modifier.fillMaxSize()) {
        DaySelector(
            dayPagerState = dayPagerState,
            weekPagerState = weekPagerState
        )

        Timetable(
            component = component,
            dayPagerState = dayPagerState,
        )
    }

    TodayButton(dayPagerState, currentTime.dayOfWeek.ordinal)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodayButton(dayPagerState: PagerState, currentDayOfWeek: Int) {
    val scope = rememberCoroutineScope()

    val todayIndex = remember {
        dayPagerState.pageCount / 2 + currentDayOfWeek
    }

    if (dayPagerState.currentPage != todayIndex) {
        Box(Modifier.fillMaxSize()) {
            Button(
                onClick = { scope.launch { dayPagerState.scrollToPage(todayIndex) } },
                modifier = Modifier
                    .size(60.dp)
                    .padding(10.dp)
                    .align(Alignment.BottomEnd),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Today",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}


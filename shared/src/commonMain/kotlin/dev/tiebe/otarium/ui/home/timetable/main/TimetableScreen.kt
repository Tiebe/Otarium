package dev.tiebe.otarium.ui.home.timetable.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import dev.tiebe.otarium.logic.root.home.children.timetable.children.timetable.TimetableComponent
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun TimetableScreen(component: TimetableComponent) {
    val dayPagerState = rememberPagerState(component.currentPage.value)
    val weekPagerState = rememberPagerState(100)

    Column(modifier = Modifier.fillMaxSize()) {
        DaySelector(
            component = component,
            dayPagerState = dayPagerState,
            weekPagerState = weekPagerState,
            dayPageCount = component.amountOfDays,
            weekPageCount = component.amountOfWeeks
        )

        Timetable(
            component = component,
            dayPagerState = dayPagerState,
            1000
        )
    }

    TodayButton(component, dayPagerState)

    var currentPage = remember { dayPagerState.currentPage }

    LaunchedEffect(dayPagerState.currentPage) {
        if (currentPage != dayPagerState.currentPage) {
            currentPage = dayPagerState.currentPage
            component.changeDay(dayPagerState.currentPage)
        }
    }

    val scope = rememberCoroutineScope()

    component.selectedWeek.subscribe { week ->
        scope.launch {
            weekPagerState.animateScrollToPage(week + 100)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodayButton(component: TimetableComponent, dayPagerState: PagerState) {
    val scope = rememberCoroutineScope()

    if (dayPagerState.currentPage != component.todayIndex) {
        Box(Modifier.fillMaxSize()) {
            Button(
                onClick = { component.scrollToPage(scope, component.todayIndex, dayPagerState) },
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
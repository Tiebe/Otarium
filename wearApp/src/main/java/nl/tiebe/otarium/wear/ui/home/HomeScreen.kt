package nl.tiebe.otarium.wear.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material3.ExperimentalWearMaterial3Api
import androidx.wear.compose.material3.HorizontalPageIndicator
import androidx.wear.compose.material3.rememberPageIndicatorState
import com.arkivanov.decompose.ComponentContext
import nl.tiebe.otarium.logic.default.home.children.grades.DefaultRecentGradesComponent
import nl.tiebe.otarium.logic.default.home.children.timetable.DefaultTimetableRootComponent
import nl.tiebe.otarium.wear.ui.home.grades.GradeScreen
import nl.tiebe.otarium.wear.ui.home.timetable.TimetableRootScreen

@OptIn(ExperimentalWearMaterial3Api::class)
@Composable
internal fun HomeScreen(componentContext: ComponentContext) {
    val pagerState = rememberPagerState { 2 }
    val pageIndicatorState = rememberPageIndicatorState(2) {
        pagerState.currentPage.toFloat() + pagerState.currentPageOffsetFraction
    }

    Scaffold(
        pageIndicator = {
            HorizontalPageIndicator(pageIndicatorState = pageIndicatorState)
        }
    ) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize(), outOfBoundsPageCount = pagerState.pageCount - 1) {
            when (it) {
                0 -> TimetableRootScreen(DefaultTimetableRootComponent(componentContext))
                1 -> GradeScreen(DefaultRecentGradesComponent(componentContext))
            }
        }
    }
}
package nl.tiebe.otarium.wear.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material3.ExperimentalWearMaterial3Api
import androidx.wear.compose.material3.HorizontalPageIndicator
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.rememberPageIndicatorState
import com.arkivanov.decompose.ComponentContext

@OptIn(ExperimentalWearMaterial3Api::class)
@Composable
internal fun HomeScreen(componentContext: ComponentContext) {
    val pagerState = rememberPagerState { 2 }
    val pageIndicatorState = rememberPageIndicatorState(2) { pagerState.currentPageOffsetFraction + 0.5f }

    Scaffold(
        pageIndicator = {
            HorizontalPageIndicator(pageIndicatorState = pageIndicatorState)
        }
    ) {
        
        Text(text = "Success!")
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) {
            when (it) {
                //1 -> TimetableRootScreen(DefaultTimetableRootComponent(componentContext))
                //2 -> GradesScreen(DefaultGradesComponent(componentContext))
            }
        }
    }
}
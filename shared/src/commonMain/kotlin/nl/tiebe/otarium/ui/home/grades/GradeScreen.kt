package nl.tiebe.otarium.ui.home.grades

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.ui.home.grades.averages.AveragesScreen
import nl.tiebe.otarium.ui.home.grades.averages.AveragesScreenTopBar
import nl.tiebe.otarium.ui.home.grades.grades.RecentGradesScreen
import nl.tiebe.otarium.ui.home.grades.grades.RecentGradesTopBar
import nl.tiebe.otarium.utils.ui.getLocalizedString

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun GradesScreen(component: GradesComponent) {
    val pagerState = rememberPagerState { 2 }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        when (pagerState.currentPage) {
            0 -> RecentGradesTopBar(component.recentGradeComponent)
            1 -> AveragesScreenTopBar(component.averagesComponent)
        }

        TabRow(
            selectedTabIndex = pagerState.currentPage,
        ) {
            Tab(
                selected = pagerState.currentPage == 0,
                onClick = { component.changeChild(GradesComponent.GradesChild.RecentGrades) },
                modifier = Modifier.height(53.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = getLocalizedString(MR.strings.gradesItem),
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp
                    )
                }
            }

            Tab(
                selected = pagerState.currentPage == 1,
                onClick = { component.changeChild(GradesComponent.GradesChild.Calculation) },
                modifier = Modifier.height(53.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = getLocalizedString(MR.strings.averagesItem),
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp
                    )
                }
            }
        }

        HorizontalPager(state = pagerState, beyondBoundsPageCount = 1, userScrollEnabled = false) { page ->
            when (page) {
                0 -> RecentGradesScreen(component.recentGradeComponent)
                1 -> AveragesScreen(component.averagesComponent)
            }
        }

        component.currentPage.observe {
            coroutineScope.launch {
                pagerState.animateScrollToPage(it)
            }
        }
    }
}
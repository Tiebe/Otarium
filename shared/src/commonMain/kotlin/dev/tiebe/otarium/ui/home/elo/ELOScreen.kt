package dev.tiebe.otarium.ui.home.elo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
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
import dev.tiebe.otarium.MR
import dev.tiebe.otarium.logic.home.children.elo.ELOComponent
import dev.tiebe.otarium.ui.home.elo.children.assignments.AssignmentsChildScreen
import dev.tiebe.otarium.ui.home.elo.children.learningresources.LearningResourcesChildScreen
import dev.tiebe.otarium.ui.home.elo.children.studyguides.StudyGuidesChildScreen
import dev.tiebe.otarium.ui.utils.tabIndicatorOffset
import dev.tiebe.otarium.utils.ui.getLocalizedString

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ELOScreen(component: ELOComponent) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(
                        pagerState,
                        3,
                        tabPositions,
                        shouldShowIndicator = true,
                        pagerState.currentPage
                    )
                )
            }
        ) {
            Tab(
                selected = pagerState.currentPage == 0,
                onClick = { component.changeChild(ELOComponent.ELOChild.StudyGuides) },
                modifier = Modifier.height(53.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = getLocalizedString(MR.strings.study_guides),
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp
                    )
                }
            }

            Tab(
                selected = pagerState.currentPage == 1,
                onClick = { component.changeChild(ELOComponent.ELOChild.Assignments) },
                modifier = Modifier.height(53.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = getLocalizedString(MR.strings.assignments),
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp
                    )
                }
            }

            Tab(
                selected = pagerState.currentPage == 2,
                onClick = { component.changeChild(ELOComponent.ELOChild.LearningResources) },
                modifier = Modifier.height(53.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = getLocalizedString(MR.strings.learning_resources),
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp
                    )
                }
            }
        }

        HorizontalPager(pageCount = 3, state = pagerState, beyondBoundsPageCount = 2, modifier = Modifier.fillMaxSize()) { page ->
            when (page) {
                0 -> StudyGuidesChildScreen(component.studyGuidesComponent)
                1 -> AssignmentsChildScreen(component.assignmentsComponent)
                2 -> LearningResourcesChildScreen(component.learningResourcesComponent)
            }
        }

        component.currentPage.subscribe {
            coroutineScope.launch {
                pagerState.animateScrollToPage(it)
            }
        }
    }
}
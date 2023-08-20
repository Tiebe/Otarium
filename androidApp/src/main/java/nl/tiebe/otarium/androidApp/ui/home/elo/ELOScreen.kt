package nl.tiebe.otarium.androidApp.ui.home.elo

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.androidApp.ui.home.elo.children.assignments.AssignmentsChildScreen
import nl.tiebe.otarium.androidApp.ui.home.elo.children.learningresources.LearningResourcesChildScreen
import nl.tiebe.otarium.androidApp.ui.home.elo.children.studyguides.StudyGuidesChildScreen
import nl.tiebe.otarium.androidApp.ui.utils.tabIndicatorOffset
import nl.tiebe.otarium.logic.root.home.children.elo.ELOComponent

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ELOScreen(component: ELOComponent) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        3
    }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
        ) {
            Tab(
                selected = pagerState.currentPage == 0,
                onClick = { component.changeChild(ELOComponent.ELOChild.StudyGuides) },
                modifier = Modifier.height(53.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(MR.strings.study_guides.resourceId),
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
                        text = stringResource(MR.strings.assignments.resourceId),
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
                        text = stringResource(MR.strings.learning_resources.resourceId),
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp
                    )
                }
            }
        }

        HorizontalPager(state = pagerState, beyondBoundsPageCount = 2, modifier = Modifier.fillMaxSize()) { page ->
            when (page) {
                0 -> StudyGuidesChildScreen(component.studyGuidesComponent)
                1 -> AssignmentsChildScreen(component.assignmentsComponent)
                2 -> LearningResourcesChildScreen(component.learningResourcesComponent)
            }
        }

        LaunchedEffect(component.currentPage.subscribeAsState()) {
            pagerState.animateScrollToPage(component.currentPage.value)
        }
    }
}
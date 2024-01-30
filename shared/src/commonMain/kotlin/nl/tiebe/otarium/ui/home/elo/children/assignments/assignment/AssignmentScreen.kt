package nl.tiebe.otarium.ui.home.elo.children.assignments.assignment

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.logic.root.home.children.elo.children.assignments.children.assignment.AssignmentScreenComponent
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
internal fun AssignmentScreen(component: AssignmentScreenComponent) {
    val assignment = component.assignment.subscribeAsState().value

    val pullRefreshState = rememberPullRefreshState(refreshing = component.isRefreshing.subscribeAsState().value, onRefresh = { component.refreshAssignment() })

    Box(Modifier.fillMaxSize().pullRefresh(pullRefreshState)) {
        val versions = component.versions.subscribeAsState().value
        val pagerState = rememberPagerState() { versions.size }

        Row(
            Modifier
                .height(20.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(versions.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(10.dp)

                )
            }
        }

        HorizontalPager(state = pagerState, pageSize = PageSize.Fixed(350.dp), contentPadding = PaddingValues(20.dp)) {
            VersionInfoScreen(component, assignment, assignment.navigationItemsVersion.reversed()[it].id,
                Modifier.width(350.dp).padding(5.dp).graphicsLayer {
                    // Calculate the absolute offset for the current page from the
                    // scroll position. We use the absolute value which allows us to mirror
                    // any effects for both directions
                    val pageOffset = (
                            (pagerState.currentPage - it) + pagerState
                                .currentPageOffsetFraction
                            ).absoluteValue

                    // We animate the alpha, between 50% and 100%
                    alpha = (1 - (1f - pageOffset.coerceIn(0f, 1f))) * 0.5f + (1f - pageOffset.coerceIn(0f, 1f)) * 1f
                }
            )
        }

        PullRefreshIndicator(
            state = pullRefreshState,
            refreshing = component.isRefreshing.subscribeAsState().value,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }

}
package nl.tiebe.otarium.androidApp.ui.utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.TabPosition
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.tabIndicatorOffset(
    dayPagerState: PagerState,
    tabPositions: List<TabPosition>,
    shouldShowIndicator: Boolean = true,
    selectedTabIndex: Int = getPage(dayPagerState, dayPagerState.pageCount)
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "tabIndicatorOffset"
        value = tabPositions[selectedTabIndex]
    }
) {
    val currentPage = minOf(tabPositions.lastIndex, selectedTabIndex)

    val currentTabPosition = tabPositions[currentPage]
    val nextTabPosition = tabPositions.getOrNull(currentPage + 1)
    val previousTabPosition = tabPositions.getOrNull(currentPage - 1)

    val fraction = dayPagerState.currentPageOffsetFraction

    var indicatorWidth: Dp
    val indicatorOffset: Dp

    if (fraction > 0 && nextTabPosition != null) {
        indicatorWidth = lerp(currentTabPosition.width, nextTabPosition.width, fraction)
        indicatorOffset = lerp(currentTabPosition.left, nextTabPosition.left, fraction)
    } else if (fraction < 0 && previousTabPosition != null) {
        indicatorWidth = lerp(currentTabPosition.width, previousTabPosition.width, -fraction)
        indicatorOffset = lerp(currentTabPosition.left, previousTabPosition.left, -fraction)
    } else {
        indicatorWidth = currentTabPosition.width
        indicatorOffset = currentTabPosition.left
    }

    if (!shouldShowIndicator) {
        indicatorWidth = 0.dp
    }

    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .width(indicatorWidth)
}


//todo FIX THIS ONE DAY
@OptIn(ExperimentalFoundationApi::class)
fun getPage(pagerState: PagerState, pageCount: Int): Int {
    return if ((pagerState.currentPage - (pageCount / 2)) % 7 >= 0) {
        (pagerState.currentPage - (pageCount / 2)) % 7
    } else {
        (pagerState.currentPage - (pageCount / 2)) % 7 + 7
    }
}
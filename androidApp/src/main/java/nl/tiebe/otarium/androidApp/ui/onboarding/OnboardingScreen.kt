package nl.tiebe.otarium.androidApp.ui.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import nl.tiebe.otarium.androidApp.ui.onboarding.sections.BottomSection
import nl.tiebe.otarium.androidApp.ui.onboarding.sections.TopSection
import nl.tiebe.otarium.logic.root.onboarding.OnboardingComponent


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun OnboardingScreen(component: OnboardingComponent) {
    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().padding(it)) {
            val items = OnboardingItems.getData()
            val scope = rememberCoroutineScope()
            val pageState = rememberPagerState {
                items.size
            }
            TopSection(
                onBackClick = {
                    if (pageState.currentPage + 1 > 1) scope.launch {
                        pageState.scrollToPage(pageState.currentPage - 1)
                    }
                },
                onSkipClick = {
                    if (pageState.currentPage + 1 < items.size) scope.launch {
                        pageState.scrollToPage(items.size - 1)
                    }
                }
            )

            HorizontalPager(
                state = pageState,
                modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .fillMaxWidth()
            ) { page ->
                OnboardingMenuItem(items = items[page])
            }
            BottomSection(size = items.size, index = pageState.currentPage) {
                if (pageState.currentPage + 1 < items.size) {
                    scope.launch {
                        pageState.animateScrollToPage(pageState.currentPage + 1)
                    }
                } else {
                    scope.launch {
                        component.notifications()
                        component.exitOnboarding()
                    }
                }
            }
        }
    }
}
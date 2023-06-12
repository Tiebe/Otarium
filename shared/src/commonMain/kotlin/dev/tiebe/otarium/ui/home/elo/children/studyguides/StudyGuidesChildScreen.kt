package dev.tiebe.otarium.ui.home.elo.children.studyguides

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Surface
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.router.stack.pop
import kotlinx.coroutines.launch
import dev.tiebe.otarium.ui.home.elo.children.studyguides.folder.StudyGuideFolderScreen
import dev.tiebe.otarium.ui.home.elo.children.studyguides.listscreen.StudyGuideListScreen

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun StudyGuidesChildScreen(component: StudyGuidesChildComponent) {
    val screen = component.childStack.subscribeAsState()
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.padding(start = 5.dp, end = 5.dp)) {
        when (val child = screen.value.items[0].instance) {
            is StudyGuidesChildComponent.Child.StudyGuideListChild -> StudyGuideListScreen(child.component)
            is StudyGuidesChildComponent.Child.FolderChild -> StudyGuideFolderScreen(child.component)
        }

        for (item in screen.value.items.subList(1, screen.value.items.size)) {
            val state = rememberDismissState()

            component.onBack.value = {
                scope.launch {
                    state.animateTo(DismissValue.DismissedToEnd)
                }
            }

            //pop on finish
            if (state.isDismissed(DismissDirection.StartToEnd)) {
                component.navigation.pop()
            }

            SwipeToDismiss(
                state = state,
                background = {
                },
                directions = setOf(DismissDirection.StartToEnd)
            ) {
                Surface(Modifier.fillMaxSize()) {
                    when (val child = item.instance) {
                        is StudyGuidesChildComponent.Child.StudyGuideListChild -> StudyGuideListScreen(child.component)
                        is StudyGuidesChildComponent.Child.FolderChild -> StudyGuideFolderScreen(child.component)
                    }
                }
            }
        }
    }
}
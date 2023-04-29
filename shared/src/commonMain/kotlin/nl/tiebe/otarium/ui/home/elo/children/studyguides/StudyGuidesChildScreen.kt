package nl.tiebe.otarium.ui.home.elo.children.studyguides

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.ui.home.elo.children.studyguides.folder.StudyGuideFolderScreen
import nl.tiebe.otarium.ui.home.elo.children.studyguides.listscreen.StudyGuideListScreen

@Composable
internal fun StudyGuidesChildScreen(component: StudyGuidesChildComponent) {
    val child = component.childStack.subscribeAsState().value

    Box(modifier = Modifier.padding(start = 5.dp, end = 5.dp)) {
        when (val screen = child.active.instance) {
            is StudyGuidesChildComponent.Child.StudyGuideListChild -> StudyGuideListScreen(screen.component)
            is StudyGuidesChildComponent.Child.FolderChild -> StudyGuideFolderScreen(screen.component)
        }
    }
}
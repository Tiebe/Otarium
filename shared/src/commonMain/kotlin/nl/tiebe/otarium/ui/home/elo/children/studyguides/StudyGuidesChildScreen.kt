package nl.tiebe.otarium.ui.home.elo.children.studyguides

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.logic.root.home.children.elo.children.studyguides.StudyGuidesChildComponent
import nl.tiebe.otarium.ui.home.elo.children.studyguides.folder.StudyGuideFolderScreen
import nl.tiebe.otarium.ui.home.elo.children.studyguides.folder.StudyGuideFolderTopAppBar
import nl.tiebe.otarium.ui.home.elo.children.studyguides.listscreen.StudyGuideListScreen
import nl.tiebe.otarium.utils.ui.getLocalizedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyGuidesTopBar(component: StudyGuidesChildComponent) {
    when (val instance = component.childStack.subscribeAsState().value.active.instance) {
        is StudyGuidesChildComponent.Child.StudyGuideListChild -> TopAppBar(title = { Text(getLocalizedString(MR.strings.study_guides)) }, windowInsets = WindowInsets(0))
        is StudyGuidesChildComponent.Child.FolderChild -> StudyGuideFolderTopAppBar(instance.component, component)
    }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
internal fun StudyGuidesChildScreen(component: StudyGuidesChildComponent) {
    Box(modifier = Modifier.padding(start = 5.dp, end = 5.dp)) {
        Children(
            component.childStack.subscribeAsState().value,
            animation = predictiveBackAnimation(
                backHandler = component.backHandler,
                animation = stackAnimation(fade() + scale()), // Your usual animation here
                onBack = component::back,
            )
        ) { child ->
            Surface(Modifier.fillMaxSize()) {
                when (val instance = child.instance) {
                    is StudyGuidesChildComponent.Child.StudyGuideListChild -> StudyGuideListScreen(instance.component)
                    is StudyGuidesChildComponent.Child.FolderChild -> StudyGuideFolderScreen(instance.component)
                }
            }
        }
    }
}
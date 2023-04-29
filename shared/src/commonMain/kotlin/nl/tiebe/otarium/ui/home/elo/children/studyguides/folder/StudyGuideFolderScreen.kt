package nl.tiebe.otarium.ui.home.elo.children.studyguides.folder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState

@Composable
internal fun StudyGuideFolderScreen(component: StudyGuideFolderComponent) {
    val contentItems = component.contentItems.subscribeAsState().value

    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState)) {
        contentItems.forEach {
            StudyGuideFolderItem(component, it)
        }
    }
}
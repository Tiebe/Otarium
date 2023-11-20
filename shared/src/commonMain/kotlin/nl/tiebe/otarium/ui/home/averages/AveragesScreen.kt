package nl.tiebe.otarium.ui.home.averages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.*
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.tiebe.magisterapi.response.general.year.grades.Subject
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.logic.root.home.children.averages.AveragesComponent
import nl.tiebe.otarium.logic.root.home.children.averages.children.AveragesListScreen
import nl.tiebe.otarium.logic.root.home.children.averages.children.AveragesListScreenTopAppBar
import nl.tiebe.otarium.ui.home.averages.subject.AverageSubjectPopup
import nl.tiebe.otarium.ui.home.averages.subject.AverageSubjectPopupTopAppBar
import nl.tiebe.otarium.utils.ui.getLocalizedString

@OptIn(ExperimentalDecomposeApi::class)
@Composable
internal fun AveragesScreen(component: AveragesComponent) {
    Column(Modifier.fillMaxSize()) {
        val subjects = listOf(Subject(-1, abbreviation = "global", description = getLocalizedString(MR.strings.global_averages), index = -1)) +
                component.gradesList.subscribeAsState().value.map { it.grade.subject }.distinct().sortedBy { it.description.lowercase() }

        Scaffold(
            topBar = {
                 when (val instance = component.childStack.subscribeAsState().value.active.instance) {
                     is AveragesComponent.Child.ListChild -> AveragesListScreenTopAppBar(component)
                     is AveragesComponent.Child.SubjectChild -> AverageSubjectPopupTopAppBar(instance.component, subjects.first { it.id == instance.subjectId })
                 }
            },
            contentWindowInsets = WindowInsets(0)
        ) {
            Children(
                component.childStack.subscribeAsState().value,
                animation = predictiveBackAnimation(
                    backHandler = component.backHandler,
                    animation = stackAnimation(fade() + scale()), // Your usual animation here
                    onBack = component::back,
                )
            ) { child ->
                Surface(modifier = Modifier.fillMaxSize().padding(it)) {
                    when (val instance = child.instance) {
                        is AveragesComponent.Child.ListChild -> AveragesListScreen(instance.component, subjects)
                        is AveragesComponent.Child.SubjectChild -> AverageSubjectPopup(
                            instance.component,
                            subjects.first { it.id == instance.subjectId },
                            component.gradesList.subscribeAsState().value
                        )
                    }
                }
            }
        }
    }
}

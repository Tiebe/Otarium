package nl.tiebe.otarium.androidApp.ui.home.timetable

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.*
import nl.tiebe.otarium.androidApp.ui.home.timetable.item.TimetableItemMembers
import nl.tiebe.otarium.androidApp.ui.home.timetable.item.TimetableItemPopup
import nl.tiebe.otarium.androidApp.ui.home.timetable.main.TimetableScreen
import nl.tiebe.otarium.logic.root.home.children.timetable.TimetableRootComponent

@OptIn(ExperimentalDecomposeApi::class)
@Composable
internal fun TimetableRootScreen(component: TimetableRootComponent) {
    Children(
        stack = component.childStack,
        animation = predictiveBackAnimation(
            backHandler = component.backHandler,
            animation = stackAnimation(fade() + scale()), // Your usual animation here
            onBack = component::back,
        ),
    ) {
        when (val child = it.instance) {
            is TimetableRootComponent.Child.TimetableChild -> {
                TimetableScreen(child.component)
            }
            is TimetableRootComponent.Child.TimetablePopupChild -> {
                TimetableItemPopup(child.component, child.id)
            }
            is TimetableRootComponent.Child.TimetableMembersChild -> {
                TimetableItemMembers(child.component, child.id)
            }
        }
    }
}
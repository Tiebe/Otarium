package nl.tiebe.otarium.ui.home.timetable

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.logic.root.home.children.timetable.TimetableRootComponent
import nl.tiebe.otarium.ui.home.timetable.item.TimetableItemPopup
import nl.tiebe.otarium.ui.home.timetable.item.TimetablePopupTopAppBar
import nl.tiebe.otarium.ui.home.timetable.main.TimetableScreen
import nl.tiebe.otarium.ui.home.timetable.main.TimetableTopAppBar

@OptIn(ExperimentalDecomposeApi::class)
@Composable
internal fun TimetableRootScreen(component: TimetableRootComponent) {
    Children(
        component.childStack.subscribeAsState().value,
        animation = predictiveBackAnimation(
            backHandler = component.backHandler,
            animation = stackAnimation(fade() + com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale()), // Your usual animation here
            onBack = component::back,
        )
    ) { child ->
        androidx.compose.material3.Scaffold(
            topBar = {
                when (val instance = child.instance) {
                    is TimetableRootComponent.Child.TimetableChild -> TimetableTopAppBar(component.timetableComponent)
                    is TimetableRootComponent.Child.TimetablePopupChild -> TimetablePopupTopAppBar(component.timetableComponent, instance.agendaItem)
                }
            },
            contentWindowInsets = WindowInsets(0)
        ) {
            Surface(modifier = Modifier.fillMaxSize().padding(it)) {
                when (val instance = child.instance) {
                    is TimetableRootComponent.Child.TimetableChild -> TimetableScreen(component.timetableComponent)
                    is TimetableRootComponent.Child.TimetablePopupChild -> TimetableItemPopup(instance.agendaItem, instance.component)
                }
            }
        }
    }

}
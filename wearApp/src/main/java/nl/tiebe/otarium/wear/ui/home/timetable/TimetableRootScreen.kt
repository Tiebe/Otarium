package nl.tiebe.otarium.wear.ui.home.timetable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import nl.tiebe.otarium.logic.root.home.children.timetable.TimetableRootComponent
import nl.tiebe.otarium.wear.ui.utils.SwipeToDismissBox

@Composable
fun TimetableRootScreen(component: TimetableRootComponent, scrollEnabled: State<Boolean>) {
    SwipeToDismissBox(stack = component.childStack, onDismissed = { component.back() }) {
        when (val instance = it.instance) {
            is TimetableRootComponent.Child.TimetableChild -> TimetableScreen(component.timetableComponent, scrollEnabled)
            is TimetableRootComponent.Child.TimetablePopupChild -> TimetableItemPopup(instance.agendaItem)
        }
    }
}
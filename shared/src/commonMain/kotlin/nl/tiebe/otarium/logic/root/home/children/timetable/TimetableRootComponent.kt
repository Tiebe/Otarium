package nl.tiebe.otarium.logic.root.home.children.timetable

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import kotlinx.serialization.Serializable
import nl.tiebe.otarium.logic.root.home.HomeComponent
import nl.tiebe.otarium.logic.root.home.children.timetable.children.timetable.TimetableComponent
import nl.tiebe.otarium.magister.AgendaItemWithAbsence

interface TimetableRootComponent : HomeComponent.MenuItemComponent, BackHandlerOwner {
    val navigation: StackNavigation<Config>
    val childStack: Value<ChildStack<Config, Child>>

    fun navigate(child: Config) {
        navigation.push(child)
    }

    fun back() {
        navigation.pop()
    }

    sealed class Child {
        class TimetableChild(val component: TimetableComponent) : Child()
        class TimetablePopupChild(val component: TimetableComponent, val agendaItem: AgendaItemWithAbsence) : Child()
    }

    @Serializable
    sealed class Config {
        @Serializable
        data object Main : Config()
        @Serializable
        data class TimetablePopup(val item: AgendaItemWithAbsence) : Config()
    }

    val timetableComponent: TimetableComponent
}

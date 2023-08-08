package nl.tiebe.otarium.logic.home.children.timetable

import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import nl.tiebe.otarium.logic.home.HomeComponent
import nl.tiebe.otarium.logic.home.children.timetable.children.timetable.TimetableComponent

interface TimetableRootComponent : HomeComponent.MenuItemComponent {
    val navigation: StackNavigation<Config>

    fun navigate(child: Config) {
        navigation.push(child)
    }

    fun back() {
        navigation.pop()
    }

    sealed class Child {
        class TimetableChild(val component: TimetableComponent) : Child()
        class TimetablePopupChild(val component: TimetableComponent, val id: Int) : Child()
    }

    sealed class Config : Parcelable {
        @Parcelize
        data object Main : Config()

        @Parcelize
        data class TimetablePopup(val id: Int) : Config()

    }

    val timetableComponent: TimetableComponent
}

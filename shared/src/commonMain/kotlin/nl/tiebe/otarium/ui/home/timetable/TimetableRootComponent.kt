package nl.tiebe.otarium.ui.home.timetable

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import nl.tiebe.otarium.ui.home.MenuItemComponent
import nl.tiebe.otarium.ui.home.timetable.main.DefaultTimetableComponent
import nl.tiebe.otarium.ui.home.timetable.main.TimetableComponent

interface TimetableRootComponent : MenuItemComponent {
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
        class TimetablePopupChild(val component: TimetableComponent, val id: Int) : Child()
    }

    sealed class Config : Parcelable {
        @Parcelize
        object Main : Config()

        @Parcelize
        data class TimetablePopup(val id: Int) : Config()

    }

    val timetableComponent: TimetableComponent
}

class DefaultTimetableRootComponent(componentContext: ComponentContext): TimetableRootComponent, ComponentContext by componentContext {
    override val navigation = StackNavigation<TimetableRootComponent.Config>()

    override val timetableComponent = DefaultTimetableComponent(componentContext, ::navigate, ::back)

    override val childStack: Value<ChildStack<TimetableRootComponent.Config, TimetableRootComponent.Child>> =
        childStack(
            source = navigation,
            initialConfiguration = TimetableRootComponent.Config.Main,
            handleBackButton = true, // Pop the back stack on back button press
            childFactory = ::createChild,
        )

    private fun createChild(config: TimetableRootComponent.Config, @Suppress("UNUSED_PARAMETER") componentContext: ComponentContext): TimetableRootComponent.Child =
        when (config) {
            is TimetableRootComponent.Config.Main -> TimetableRootComponent.Child.TimetableChild(timetableComponent)
            is TimetableRootComponent.Config.TimetablePopup -> TimetableRootComponent.Child.TimetablePopupChild(timetableComponent, config.id)
        }
}
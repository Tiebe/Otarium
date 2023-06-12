package dev.tiebe.otarium.ui.home.timetable

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import dev.tiebe.otarium.ui.home.MenuItemComponent
import dev.tiebe.otarium.ui.home.timetable.main.DefaultTimetableComponent
import dev.tiebe.otarium.ui.home.timetable.main.TimetableComponent

interface TimetableRootComponent : MenuItemComponent {
    val navigation: StackNavigation<Config>
    val childStack: Value<ChildStack<Config, Child>>

    val onBack: MutableValue<BackCallback>

    fun registerBackHandler()
    fun unregisterBackHandler()

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
    override val onBack: MutableValue<BackCallback> = MutableValue(BackCallback { back() })

    override val timetableComponent = DefaultTimetableComponent(componentContext, ::navigate, onBack)

    override val childStack: Value<ChildStack<TimetableRootComponent.Config, TimetableRootComponent.Child>> =
        childStack(
            source = navigation,
            initialConfiguration = TimetableRootComponent.Config.Main,
            handleBackButton = false, // Pop the back stack on back button press
            childFactory = ::createChild,
        )

    override fun registerBackHandler() {
        backHandler.register(onBack.value)
    }

    override fun unregisterBackHandler() {
        backHandler.unregister(onBack.value)
    }

    private fun createChild(config: TimetableRootComponent.Config, @Suppress("UNUSED_PARAMETER") componentContext: ComponentContext): TimetableRootComponent.Child =
        when (config) {
            is TimetableRootComponent.Config.Main -> TimetableRootComponent.Child.TimetableChild(timetableComponent)
            is TimetableRootComponent.Config.TimetablePopup -> TimetableRootComponent.Child.TimetablePopupChild(timetableComponent, config.id)
        }

    init {
        backHandler.register(onBack.value)
    }
}
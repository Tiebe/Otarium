package nl.tiebe.otarium.logic.default.home.children.timetable

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import nl.tiebe.otarium.logic.default.home.children.timetable.children.timetable.DefaultTimetableComponent
import nl.tiebe.otarium.logic.root.home.children.timetable.TimetableRootComponent

class DefaultTimetableRootComponent(componentContext: ComponentContext): TimetableRootComponent, ComponentContext by componentContext, BackHandlerOwner {
    override val navigation = StackNavigation<TimetableRootComponent.Config>()

    override val timetableComponent = DefaultTimetableComponent(componentContext, this)

    override val childStack: Value<ChildStack<TimetableRootComponent.Config, TimetableRootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = TimetableRootComponent.Config.serializer(),
            initialConfiguration = TimetableRootComponent.Config.Main,
            handleBackButton = false, // Pop the back stack on back button press
            childFactory = ::createChild,
        )

    private fun createChild(config: TimetableRootComponent.Config, @Suppress("UNUSED_PARAMETER") componentContext: ComponentContext): TimetableRootComponent.Child =
        when (config) {
            is TimetableRootComponent.Config.Main -> TimetableRootComponent.Child.TimetableChild(timetableComponent)
            is TimetableRootComponent.Config.TimetablePopup -> TimetableRootComponent.Child.TimetablePopupChild(
                timetableComponent,
                config.item
            )
        }
}
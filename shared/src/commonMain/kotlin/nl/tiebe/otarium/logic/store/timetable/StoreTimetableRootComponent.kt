package nl.tiebe.otarium.logic.store.timetable

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import nl.tiebe.otarium.logic.root.home.children.timetable.TimetableRootComponent
import nl.tiebe.otarium.logic.store.timetable.children.timetable.StoreTimetableComponent

class StoreTimetableRootComponent(componentContext: ComponentContext): TimetableRootComponent, ComponentContext by componentContext,
    BackHandlerOwner {
    override val navigation = StackNavigation<TimetableRootComponent.Config>()

    override val timetableComponent = StoreTimetableComponent(componentContext, ::navigate, ::back)

    override val childStack: Value<ChildStack<TimetableRootComponent.Config, TimetableRootComponent.Child>> =
        childStack(
            source = navigation,
            initialConfiguration = TimetableRootComponent.Config.Main,
            handleBackButton = false, // Pop the back stack on back button press
            childFactory = ::createChild,
        )

    private fun createChild(config: TimetableRootComponent.Config, @Suppress("UNUSED_PARAMETER") componentContext: ComponentContext): TimetableRootComponent.Child =
        when (config) {
            is TimetableRootComponent.Config.Main -> TimetableRootComponent.Child.TimetableChild(timetableComponent)
            is TimetableRootComponent.Config.TimetablePopup -> TimetableRootComponent.Child.TimetablePopupChild(
                timetableComponent,
                config.id
            )
            is TimetableRootComponent.Config.TimetableMembers -> TimetableRootComponent.Child.TimetableMembersChild(
                timetableComponent,
                config.id
            )
        }
}
package nl.tiebe.otarium.logic.store

import com.arkivanov.decompose.ComponentContext
import nl.tiebe.otarium.logic.default.home.DefaultHomeComponent
import nl.tiebe.otarium.logic.root.RootComponent
import nl.tiebe.otarium.logic.root.home.MenuItems

class StoreHomeComponent(componentContext: ComponentContext, override val navigateRootComponent: (RootComponent.ChildScreen) -> Unit
): DefaultHomeComponent(componentContext, navigateRootComponent) {
    override val visibleItems: List<MenuItems> = listOf(
        MenuItems.Timetable,
        MenuItems.Grades,
        MenuItems.Settings
    )

/*
    override fun timetableComponent(componentContext: ComponentContext) =
        StoreTimetableComponent(
            componentContext = componentContext,
        )


    override fun gradesComponent(componentContext: ComponentContext) =
        StoreGradeComponent(
            componentContext = componentContext
        )

    override fun settingsComponent(componentContext: ComponentContext) =
        StoreSettingsComponent(
            componentContext = componentContext,
            navigateRootComponent = navigateRootComponent
        )*/
}
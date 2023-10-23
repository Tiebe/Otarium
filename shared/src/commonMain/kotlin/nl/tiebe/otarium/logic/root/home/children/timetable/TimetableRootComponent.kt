package nl.tiebe.otarium.logic.root.home.children.timetable

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import nl.tiebe.otarium.logic.root.home.HomeComponent
import nl.tiebe.otarium.logic.root.home.children.other.contact.ContactComponent
import nl.tiebe.otarium.logic.root.home.children.timetable.children.timetable.TimetableComponent

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
        class TimetablePopupChild(val component: TimetableComponent, val id: Int) : Child()
        class TimetableMembersChild(val component: TimetableComponent, val id: Int) : Child()
        class ContactInfoChild(val component: ContactComponent, val name: String) : Child()
    }

    sealed class Config : Parcelable {
        @Parcelize
        object Main : Config()

        @Parcelize
        data class TimetablePopup(val id: Int) : Config()

        @Parcelize
        data class TimetableMembers(val id: Int) : Config()

        @Parcelize
        data class ContactInfo(val name: String): Config()

    }

    val timetableComponent: TimetableComponent
}

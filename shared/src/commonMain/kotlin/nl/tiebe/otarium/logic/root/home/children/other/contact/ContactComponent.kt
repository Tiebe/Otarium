package nl.tiebe.otarium.logic.root.home.children.other.contact

import com.arkivanov.decompose.value.MutableValue
import dev.tiebe.magisterapi.response.profileinfo.Contact
import nl.tiebe.otarium.logic.root.home.HomeComponent

interface ContactComponent: HomeComponent.MenuItemComponent {
    val contact: MutableValue<Contact>

    suspend fun getContactInfo(name: String): Contact?
}
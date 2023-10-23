package nl.tiebe.otarium.logic.default.home.children.other.contact

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import dev.tiebe.magisterapi.api.account.ProfileInfoFlow
import dev.tiebe.magisterapi.response.profileinfo.Contact
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.logic.root.home.children.other.contact.ContactComponent

class DefaultContactComponent(componentContext: ComponentContext, val name: String) : ContactComponent, ComponentContext by componentContext {
    override val contact: MutableValue<Contact> = MutableValue(Contact())

    override suspend fun getContactInfo(name: String): Contact? {
        return ProfileInfoFlow.getContacts(Data.selectedAccount.tenantUrl, Data.selectedAccount.tokens.accessToken, name).firstOrNull().also { contact.value = it ?: Contact(-1) }
    }
}
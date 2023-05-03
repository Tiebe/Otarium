package nl.tiebe.otarium.store.component.home.children.settings.children

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.response.profileinfo.Person
import dev.tiebe.magisterapi.response.profileinfo.ProfileInfo
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.magister.MagisterAccount
import nl.tiebe.otarium.ui.home.settings.SettingsComponent
import nl.tiebe.otarium.ui.home.settings.items.users.UserChildComponent
import nl.tiebe.otarium.ui.login.DefaultLoginComponent
import nl.tiebe.otarium.ui.root.RootComponent

class StoreUserChildComponent(
    componentContext: ComponentContext,
    private val _navigate: (child: SettingsComponent.Config) -> Unit,
    val navigateRootComponent: (RootComponent.ChildScreen) -> Unit
) : UserChildComponent, ComponentContext by componentContext {
    override val users: Value<List<MagisterAccount>> = MutableValue(listOf(
        MagisterAccount(
            1,
            ProfileInfo(
                "",
                Person(
                    1,
                    "Apple",
                    null,
                    "Developer",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    false
                ),
                listOf(),
                listOf()
            ),
            ""
        )
    ))
    override val selectedAccount: Value<Int> = MutableValue(Data.selectedAccount.accountId)

    override fun navigate(child: SettingsComponent.Config) {
        _navigate(child)
    }

    override fun removeAccount(accountId: Int) {
        openLoginScreen()
    }

    override val openLoginScreen: () -> Unit = {
        navigateRootComponent(RootComponent.ChildScreen.LoginChild(
            DefaultLoginComponent(
                componentContext = this,
                navigateRootComponent
            )
        ))
    }

}
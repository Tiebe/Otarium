package nl.tiebe.otarium.ui.home.settings.items.users

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.magister.MagisterAccount
import nl.tiebe.otarium.settings
import nl.tiebe.otarium.ui.home.settings.SettingsComponent
import nl.tiebe.otarium.ui.login.DefaultLoginComponent
import nl.tiebe.otarium.ui.root.RootComponent

interface UserChildComponent {
    val users: Value<List<MagisterAccount>>
    val selectedAccount: Value<Int>

    fun navigate(child: SettingsComponent.Config)
    fun removeAccount(accountId: Int)

    val openLoginScreen: () -> Unit

}

class DefaultUserChildComponent(
    componentContext: ComponentContext,
    private val _navigate: (child: SettingsComponent.Config) -> Unit,
    val navigateRootComponent: (RootComponent.ChildScreen) -> Unit
) : UserChildComponent, ComponentContext by componentContext {
    override val users: MutableValue<List<MagisterAccount>> = MutableValue(Data.accounts)
    override val selectedAccount: MutableValue<Int> = MutableValue(Data.selectedAccount.accountId)

    override fun navigate(child: SettingsComponent.Config) {
        _navigate(child)
    }

    override val openLoginScreen: () -> Unit = {
        navigateRootComponent(RootComponent.ChildScreen.LoginChild(
            DefaultLoginComponent(
                componentContext = this,
                navigateRootComponent
            )
        ))
    }

    override fun removeAccount(accountId: Int) {
        settings.remove("agenda-${accountId}")
        settings.remove("grades-${accountId}")
        settings.remove("full_grade_list-${accountId}")
        settings.remove("tokens-${accountId}")
        Data.accounts = Data.accounts.filter { it.accountId != accountId }
        users.value = Data.accounts

        if (users.value.isEmpty()) {
            openLoginScreen()
        }
        else if (Data.selectedAccount.accountId == accountId) {
            Data.selectedAccount = Data.accounts.first { it.accountId != accountId }
        }
    }

}
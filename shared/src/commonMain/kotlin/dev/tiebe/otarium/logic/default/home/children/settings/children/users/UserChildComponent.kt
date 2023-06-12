package dev.tiebe.otarium.logic.default.home.children.settings.children.users

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import dev.tiebe.otarium.Data
import dev.tiebe.otarium.logic.default.login.DefaultLoginComponent
import dev.tiebe.otarium.logic.root.RootComponent
import dev.tiebe.otarium.logic.root.home.children.settings.SettingsComponent
import dev.tiebe.otarium.logic.root.home.children.settings.children.users.UserChildComponent
import dev.tiebe.otarium.magister.MagisterAccount
import dev.tiebe.otarium.settings


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
        navigateRootComponent(
            RootComponent.ChildScreen.LoginChild(
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

    override fun selectAccount(account: MagisterAccount) {
        Data.selectedAccount = account;
        selectedAccount.value = account.accountId
    }

}
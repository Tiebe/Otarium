package dev.tiebe.otarium.logic.root.home.children.settings.children.users

import com.arkivanov.decompose.value.Value
import dev.tiebe.otarium.logic.root.home.children.settings.SettingsComponent
import dev.tiebe.otarium.magister.MagisterAccount

interface UserChildComponent {
    val users: Value<List<MagisterAccount>>
    val selectedAccount: Value<Int>

    fun navigate(child: SettingsComponent.Config)
    fun removeAccount(accountId: Int)
    fun selectAccount(account: MagisterAccount)

    val openLoginScreen: () -> Unit

}
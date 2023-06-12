package dev.tiebe.otarium.logic.default.home.children.debug

import com.arkivanov.decompose.ComponentContext
import dev.icerock.moko.resources.desc.StringDesc
import dev.tiebe.magisterapi.response.TokenResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import dev.tiebe.otarium.Data
import dev.tiebe.otarium.magister.getAccount
import dev.tiebe.otarium.logic.default.RootComponent
import dev.tiebe.otarium.logic.default.componentCoroutineScope
import dev.tiebe.otarium.utils.copyToClipboard


class DefaultDebugComponent(
    componentContext: ComponentContext,
    override val navigateRootComponent: (RootComponent.ChildScreen) -> Unit,
): DebugComponent, ComponentContext by componentContext {
    override val scope: CoroutineScope = componentCoroutineScope()


}
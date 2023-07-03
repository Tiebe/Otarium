package nl.tiebe.otarium.logic.default.home.children.debug

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import nl.tiebe.otarium.logic.default.componentCoroutineScope
import nl.tiebe.otarium.logic.root.RootComponent
import nl.tiebe.otarium.logic.root.home.children.debug.DebugComponent

class DefaultDebugComponent(
    componentContext: ComponentContext,
    override val navigateRootComponent: (RootComponent.ChildScreen) -> Unit,
): DebugComponent, ComponentContext by componentContext {
    override val scope: CoroutineScope = componentCoroutineScope()


}
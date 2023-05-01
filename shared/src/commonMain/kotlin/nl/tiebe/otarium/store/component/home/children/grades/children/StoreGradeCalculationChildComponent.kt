package nl.tiebe.otarium.store.component.home.children.grades.children

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.backhandler.BackCallback
import dev.tiebe.magisterapi.response.general.year.grades.Subject
import kotlinx.coroutines.launch
import nl.tiebe.otarium.ui.home.grades.calculation.GradeCalculationChildComponent
import nl.tiebe.otarium.ui.root.componentCoroutineScope

class StoreGradeCalculationChildComponent(componentContext: ComponentContext) : GradeCalculationChildComponent, ComponentContext by componentContext {
    override val openedSubject: MutableValue<Pair<Boolean, Subject?>> = MutableValue(false to null)
    override val backCallbackOpenItem = BackCallback(false) {
        closeSubject()
    }

    override val state: MutableValue<GradeCalculationChildComponent.State> =
        MutableValue(GradeCalculationChildComponent.State.Loading)
    private val scope = componentCoroutineScope()

    init {
        backHandler.register(backCallbackOpenItem)

        scope.launch {
            state.value = GradeCalculationChildComponent.State.Failed
        }
    }
}
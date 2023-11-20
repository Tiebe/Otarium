package nl.tiebe.otarium.logic.default.home.children.averages

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import dev.tiebe.magisterapi.response.general.year.grades.GradeColumn
import kotlinx.coroutines.launch
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.logic.default.componentCoroutineScope
import nl.tiebe.otarium.logic.root.home.children.averages.AveragesComponent
import nl.tiebe.otarium.magister.GradeWithGradeInfo
import nl.tiebe.otarium.magister.ManualGrade
import nl.tiebe.otarium.magister.refreshGrades

class DefaultAveragesComponent(componentContext: ComponentContext) : AveragesComponent, ComponentContext by componentContext,
    BackHandlerOwner {
    override val refreshState: MutableValue<Boolean> = MutableValue(false)

    override fun refreshGrades() {
        scope.launch {
            refreshState.value = true
            Data.selectedAccount.fullGradeList = Data.selectedAccount.refreshGrades()
            gradesList.value = Data.selectedAccount.fullGradeList.filter {
                it.grade.gradeColumn.type == GradeColumn.Type.Grade &&
                        it.grade.grade?.replace(",", ".")?.toDoubleOrNull() != null
            }
            refreshState.value = false
        }
    }

    override val navigation = StackNavigation<AveragesComponent.Config>()

    override val childStack: Value<ChildStack<AveragesComponent.Config, AveragesComponent.Child>> =
        childStack(
            source = navigation,
            initialConfiguration = AveragesComponent.Config.List,
            handleBackButton = false, // Pop the back stack on back button press
            childFactory = ::createChild,
        )

    private fun createChild(config: AveragesComponent.Config, componentContext: ComponentContext): AveragesComponent.Child =
        when (config) {
            is AveragesComponent.Config.List -> AveragesComponent.Child.ListChild(this)
            is AveragesComponent.Config.Subject -> AveragesComponent.Child.SubjectChild(this, config.subjectId)
        }


    override val gradesList: MutableValue<List<GradeWithGradeInfo>> = MutableValue(
        Data.selectedAccount.fullGradeList.filter {
            it.grade.gradeColumn.type == GradeColumn.Type.Grade &&
                    it.grade.grade?.replace(",", ".")?.toDoubleOrNull() != null
        }
    )
    override val cardList = MutableValue(Data.cardList)

    override val manualGradesList: MutableValue<List<ManualGrade>> = MutableValue(Data.manualGrades)
    override val addManualGradePopupOpen: MutableValue<Boolean> = MutableValue(false)
    override fun addManualGrade(manualGrade: ManualGrade) {
        manualGradesList.value = manualGradesList.value.toMutableList().apply {
            add(manualGrade)
        }
        Data.manualGrades = manualGradesList.value
    }

    override fun removeManualGrade(manualGrade: ManualGrade) {
        manualGradesList.value = manualGradesList.value.toMutableList().apply {
            remove(manualGrade)
        }
        Data.manualGrades = manualGradesList.value
    }

    override fun back() {
        navigation.pop()
    }

    private val scope = componentCoroutineScope()

    init {
        refreshGrades()
    }
}
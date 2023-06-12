package dev.tiebe.otarium.logic.default.home.children.settings.children.ui

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.tiebe.otarium.Data
import dev.tiebe.otarium.logic.home.children.settings.SettingsComponent

class DefaultUIChildComponent(
    componentContext: ComponentContext,
    private val _navigate: (child: SettingsComponent.Config) -> Unit
) : UIChildComponent, ComponentContext by componentContext {
    override fun navigate(child: SettingsComponent.Config) {
        _navigate(child)
    }

    override val showCancelledLessons: MutableValue<Boolean> = MutableValue(Data.showCancelledLessons)
    override fun showCancelledLessons(value: Boolean) {
        Data.showCancelledLessons = value
        showCancelledLessons.value = value
    }

    override val markGrades: MutableValue<Boolean> = MutableValue(Data.markGrades)
    override fun markGrades(value: Boolean) {
        Data.markGrades = value
        markGrades.value = value
    }

    override val passingGrade: MutableValue<String> = MutableValue(Data.passingGrade.toString())
    override fun passingGrade(value: String) {
        Data.passingGrade = value.toFloatOrNull() ?: 5.5f
        passingGrade.value = value
    }

}
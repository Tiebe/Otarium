package nl.tiebe.otarium.logic.root.home.children.settings.children.ui

import com.arkivanov.decompose.value.Value
import nl.tiebe.otarium.logic.root.home.children.settings.SettingsComponent

interface UIChildComponent {
    fun navigate(child: SettingsComponent.Config)

    val showCancelledLessons: Value<Boolean>

    fun showCancelledLessons(value: Boolean)

    val markGrades: Value<Boolean>

    fun markGrades(value: Boolean)

    val passingGrade: Value<String>

    fun passingGrade(value: String)


}
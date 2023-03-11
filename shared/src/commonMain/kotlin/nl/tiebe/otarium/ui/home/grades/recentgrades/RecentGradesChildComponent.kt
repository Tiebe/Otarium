package nl.tiebe.otarium.ui.home.grades.recentgrades

import com.arkivanov.decompose.ComponentContext
import nl.tiebe.otarium.ui.home.grades.GradesChildComponent

interface RecentGradesChildComponent : GradesChildComponent {
}

class DefaultRecentGradesChildComponent(componentContext: ComponentContext) : RecentGradesChildComponent, ComponentContext by componentContext {


}
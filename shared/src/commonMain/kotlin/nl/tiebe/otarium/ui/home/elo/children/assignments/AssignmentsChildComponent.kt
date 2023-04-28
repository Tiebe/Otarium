package nl.tiebe.otarium.ui.home.elo.children.assignments

import com.arkivanov.decompose.ComponentContext
import nl.tiebe.otarium.ui.home.elo.ELOChildComponent

interface AssignmentsChildComponent : ELOChildComponent {

}

class DefaultAssignmentsChildComponent(componentContext: ComponentContext) : AssignmentsChildComponent, ComponentContext by componentContext {
}
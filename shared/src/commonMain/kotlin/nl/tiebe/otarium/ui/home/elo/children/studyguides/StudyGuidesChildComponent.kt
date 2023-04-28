package nl.tiebe.otarium.ui.home.elo.children.studyguides

import com.arkivanov.decompose.ComponentContext
import nl.tiebe.otarium.ui.home.elo.ELOChildComponent

interface StudyGuidesChildComponent : ELOChildComponent {

}

class DefaultStudyGuidesChildComponent(componentContext: ComponentContext) : StudyGuidesChildComponent, ComponentContext by componentContext {
}
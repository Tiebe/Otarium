package nl.tiebe.otarium.ui.home.elo.children.learningresources

import com.arkivanov.decompose.ComponentContext
import nl.tiebe.otarium.ui.home.elo.ELOChildComponent

interface LearningResourcesChildComponent : ELOChildComponent {

}

class DefaultLearningResourcesChildComponent(componentContext: ComponentContext) : LearningResourcesChildComponent, ComponentContext by componentContext {

}
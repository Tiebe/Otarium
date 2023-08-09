package nl.tiebe.otarium.logic.home.children.elo.children.learningresources

import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.response.learningresource.LearningResource
import nl.tiebe.otarium.logic.home.children.elo.ELOComponent

/**
 * A child component of the ELO component that handles the learning resources.
 *
 * @param LearningResource The type of learning resource.
 */
interface LearningResourcesChildComponent<LearningResource>: ELOComponent.ELOChildComponent {
    /** The learning resources. */
    val learningResources: Value<List<LearningResource>>

    /**
     * Refreshes the learning resources.
     *
     * @return The refreshed learning resources. These should also be stored in [learningResources].
     */
    fun refreshLearningResources(): List<LearningResource>

    /**
     * Opens the learning resource link in the default browser.
     *
     * @param learningResource The learning resource to open.
     */
    fun openLearningResource(learningResource: LearningResource)

}
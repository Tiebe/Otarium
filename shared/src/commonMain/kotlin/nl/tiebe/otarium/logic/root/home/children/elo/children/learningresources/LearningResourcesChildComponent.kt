package nl.tiebe.otarium.logic.root.home.children.elo.children.learningresources

import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.api.learningresource.LearningResourceFlow
import dev.tiebe.magisterapi.response.learningresource.LearningResource
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.logic.root.home.children.elo.ELOComponent

interface LearningResourcesChildComponent : ELOComponent.ELOChildComponent {
    val learningResources: Value<List<LearningResource>>
    val isRefreshing: Value<Boolean>

    val scope: CoroutineScope

    fun refreshLearningResources()

    fun openLearningResource(learningResource: LearningResource) {
        scope.launch {
            val url = LearningResourceFlow.getLearningResourceUrl(
                Url(Data.selectedAccount.tenantUrl),
                Data.selectedAccount.tokens.accessToken,
                learningResource.links.first { it.rel == "content" }.href
            )

            //TODO: openUrl(url ?: return@launch)
        }
    }

}
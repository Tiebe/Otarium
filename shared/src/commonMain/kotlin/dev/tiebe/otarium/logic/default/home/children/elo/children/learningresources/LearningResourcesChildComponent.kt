package dev.tiebe.otarium.logic.default.home.children.elo.children.learningresources

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import dev.tiebe.magisterapi.api.learningresource.LearningResourceFlow
import dev.tiebe.magisterapi.response.learningresource.LearningResource
import dev.tiebe.otarium.Data
import dev.tiebe.otarium.logic.default.componentCoroutineScope
import dev.tiebe.otarium.logic.root.home.children.elo.children.learningresources.LearningResourcesChildComponent
import io.ktor.http.*
import kotlinx.coroutines.launch

class DefaultLearningResourcesChildComponent(componentContext: ComponentContext) : LearningResourcesChildComponent, ComponentContext by componentContext {
    override val learningResources: MutableValue<List<LearningResource>> = MutableValue(emptyList())
    override val isRefreshing: MutableValue<Boolean> = MutableValue(false)

    override val scope = componentCoroutineScope()

    override fun refreshLearningResources() {
        scope.launch {
            isRefreshing.value = true
            learningResources.value = LearningResourceFlow.getLearningResources(Url(Data.selectedAccount.tenantUrl), Data.selectedAccount.tokens.accessToken, Data.selectedAccount.accountId)
            isRefreshing.value = false
        }
    }

    init {
        refreshLearningResources()
    }

}
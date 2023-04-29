package nl.tiebe.otarium.ui.home.elo.children.studyguides.folder

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.api.StudyGuideFlow
import dev.tiebe.magisterapi.response.studyguide.StudyGuideContent
import dev.tiebe.magisterapi.response.studyguide.StudyGuideContentItem
import io.ktor.http.*
import kotlinx.coroutines.launch
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.ui.root.componentCoroutineScope

interface StudyGuideFolderComponent {
    val content: Value<StudyGuideContent>
    val contentItems: Value<List<StudyGuideContentItem>>
    val studyGuideLink: String

}

class DefaultStudyGuideFolderComponent(componentContext: ComponentContext, override val studyGuideLink: String) : StudyGuideFolderComponent, ComponentContext by componentContext {
    override val content: MutableValue<StudyGuideContent> = MutableValue(StudyGuideContent(0, false, false, null, StudyGuideContent.Companion.Contents(listOf(), listOf(), 0), "", "", listOf(), ""))
    override val contentItems: MutableValue<List<StudyGuideContentItem>> = MutableValue(listOf())

    val scope = componentCoroutineScope()

    private fun loadContent() {
        scope.launch {
            content.value = StudyGuideFlow.getStudyGuideContent(Url(Data.selectedAccount.tenantUrl), Data.selectedAccount.tokens.accessToken, studyGuideLink)

            val items = mutableListOf<StudyGuideContentItem>()

            for (item in content.value.contents.items) {
                items.add(
                    StudyGuideFlow.getStudyGuideContentItem(
                        Url(Data.selectedAccount.tenantUrl),
                        Data.selectedAccount.tokens.accessToken,
                        item.links.first { it.rel == "Self" }.href
                    )
                )
            }

            contentItems.value = items
        }
    }

    init {
        loadContent()
    }
}
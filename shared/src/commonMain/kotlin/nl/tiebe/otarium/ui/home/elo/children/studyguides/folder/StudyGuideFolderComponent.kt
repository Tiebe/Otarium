package nl.tiebe.otarium.ui.home.elo.children.studyguides.folder

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.api.StudyGuideFlow
import dev.tiebe.magisterapi.response.studyguide.Resource
import dev.tiebe.magisterapi.response.studyguide.StudyGuideContent
import dev.tiebe.magisterapi.response.studyguide.StudyGuideContentItem
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.launch
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.ui.root.componentCoroutineScope
import nl.tiebe.otarium.utils.getDownloadFileLocation
import nl.tiebe.otarium.utils.openFileFromCache
import nl.tiebe.otarium.utils.requestGET

interface StudyGuideFolderComponent {
    val content: Value<StudyGuideContent>
    val contentItems: Value<List<StudyGuideContentItem>>
    val studyGuideLink: String

    val resourceDownloadProgress: Value<Map<Int, Float>>

    val refreshing: Value<Boolean>

    fun downloadResource(item: Resource)

    fun loadContent()
}

class DefaultStudyGuideFolderComponent(componentContext: ComponentContext, override val studyGuideLink: String) : StudyGuideFolderComponent, ComponentContext by componentContext {
    override val content: MutableValue<StudyGuideContent> = MutableValue(StudyGuideContent(0, false, false, null, StudyGuideContent.Companion.Contents(listOf(), listOf(), 0), "", "", listOf(), ""))
    override val contentItems: MutableValue<List<StudyGuideContentItem>> = MutableValue(listOf())

    override val resourceDownloadProgress: MutableValue<Map<Int, Float>> = MutableValue(mapOf())
    override val refreshing: MutableValue<Boolean> = MutableValue(false)

    val scope = componentCoroutineScope()

    override fun loadContent() {
        scope.launch {
            refreshing.value = true
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
            refreshing.value = false
        }
    }

    override fun downloadResource(item: Resource) {
        scope.launch {
            val response = requestGET(
                URLBuilder(Data.selectedAccount.tenantUrl).appendEncodedPathSegments(item.links.first { it.rel == "Contents" }.href).build(),
                accessToken = Data.selectedAccount.tokens.accessToken,
                onDownload = { bytesSentTotal, contentLength ->
                    resourceDownloadProgress.value = resourceDownloadProgress.value + Pair(item.id, bytesSentTotal.toFloat() / contentLength.toFloat())
                }
            ).bodyAsChannel()

            response.copyAndClose(getDownloadFileLocation(item.id.toString(), item.name))
            openFileFromCache(item.id.toString(), item.name)

            resourceDownloadProgress.value = resourceDownloadProgress.value.toMutableMap().also {
                it.remove(item.id)
            }
        }
    }

    init {
        loadContent()
    }
}
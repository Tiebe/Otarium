package nl.tiebe.otarium.logic.home.children.elo.children.studyguides.children.folder

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.api.studyguide.StudyGuideFlow
import dev.tiebe.magisterapi.response.studyguide.Resource
import dev.tiebe.magisterapi.response.studyguide.StudyGuideContent
import dev.tiebe.magisterapi.response.studyguide.StudyGuideContentItem
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.logic.default.componentCoroutineScope
import nl.tiebe.otarium.utils.openFileFromCache
import nl.tiebe.otarium.utils.requestGET
import nl.tiebe.otarium.utils.writeFile
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.launch

interface StudyGuideFolderComponent {
    val content: Value<StudyGuideContent>
    val contentItems: Value<List<StudyGuideContentItem>>
    val studyGuideLink: String

    val resourceDownloadProgress: Value<Map<Int, Float>>

    val refreshing: Value<Boolean>

    fun downloadResource(item: Resource)

    fun loadContent()
}
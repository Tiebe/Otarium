package nl.tiebe.otarium.logic.root.home.children.elo.children.studyguides.children.folder

import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.response.studyguide.Resource
import dev.tiebe.magisterapi.response.studyguide.StudyGuideContent
import dev.tiebe.magisterapi.response.studyguide.StudyGuideContentItem

interface StudyGuideFolderComponent {
    val content: Value<StudyGuideContent>
    val contentItems: Value<List<StudyGuideContentItem>>
    val studyGuideLink: String

    val resourceDownloadProgress: Value<Map<Int, Float>>

    val refreshing: Value<Boolean>

    fun downloadResource(item: Resource)

    fun loadContent()
}
package nl.tiebe.otarium.logic.home.children.elo.children.studyguides.children.folder

import com.arkivanov.decompose.value.Value

interface StudyGuideFolderComponent<StudyGuide, StudyGuideContent: Any, StudyGuideResource> {
    val content: Value<StudyGuideContent>
    val studyGuide: StudyGuide

    /** The study guide resource download progress. A map from the resource id to the progress float (0.0-1.0) */
    val resourceDownloadProgress: Value<Map<Int, Float>>

    /**
     * Download a resource. The progress can be tracked using [resourceDownloadProgress].
     *
     * @param item The resource to download.
     */
    fun downloadResource(item: StudyGuideResource)

    /**
     * Load the content of this study guide.
     */
    fun loadContent()
}
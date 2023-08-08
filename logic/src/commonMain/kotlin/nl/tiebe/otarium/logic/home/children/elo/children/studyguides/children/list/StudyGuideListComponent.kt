package nl.tiebe.otarium.logic.home.children.elo.children.studyguides.children.list

import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.response.studyguide.StudyGuide
import nl.tiebe.otarium.logic.root.home.children.elo.children.studyguides.StudyGuidesChildComponent

interface StudyGuideListComponent : StudyGuidesChildComponent.StudyGuideChildScreen {
    val studyGuides: Value<List<StudyGuide>>
    val parentComponent: StudyGuidesChildComponent

    val isRefreshing: Value<Boolean>

    fun refreshStudyGuides()

    fun navigateToStudyGuide(studyGuide: StudyGuide) {
        parentComponent.navigate(StudyGuidesChildComponent.Config.StudyGuide(studyGuide.links.first { it.rel == "Self" }.href))
    }
}
package nl.tiebe.otarium.ui.home.elo.children.studyguides.listscreen

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.api.studyguide.StudyGuideFlow
import dev.tiebe.magisterapi.response.studyguide.StudyGuide
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.ui.home.elo.children.studyguides.StudyGuideChildScreen
import nl.tiebe.otarium.ui.home.elo.children.studyguides.StudyGuidesChildComponent
import nl.tiebe.otarium.ui.root.componentCoroutineScope
import nl.tiebe.otarium.utils.toFormattedString

interface StudyGuideListComponent : StudyGuideChildScreen {
    val studyGuides: Value<List<StudyGuide>>
    val parentComponent: StudyGuidesChildComponent

    val isRefreshing: Value<Boolean>

    fun refreshStudyGuides()

    fun navigateToStudyGuide(studyGuide: StudyGuide) {
        parentComponent.navigate(StudyGuidesChildComponent.Config.StudyGuide(studyGuide.links.first { it.rel == "Self" }.href))
    }
}

class DefaultStudyGuideListComponent(
    componentContext: ComponentContext,
    override val parentComponent: StudyGuidesChildComponent
) : StudyGuideListComponent, ComponentContext by componentContext {
    override val studyGuides: MutableValue<List<StudyGuide>> = MutableValue(listOf())

    override val isRefreshing: MutableValue<Boolean> = MutableValue(false)

    val scope = componentCoroutineScope()

    override fun refreshStudyGuides() {
        scope.launch {
            isRefreshing.value = true
            val date = Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam"))

            studyGuides.value = StudyGuideFlow.getFullStudyGuideList(
                Url(Data.selectedAccount.tenantUrl),
                Data.selectedAccount.tokens.accessToken,
                Data.selectedAccount.accountId,
                "${date.year.toFormattedString()}-${date.monthNumber.toFormattedString()}-${date.dayOfMonth.toFormattedString()}"
            )
            isRefreshing.value = false
        }
    }

    init {
        refreshStudyGuides()
    }
}
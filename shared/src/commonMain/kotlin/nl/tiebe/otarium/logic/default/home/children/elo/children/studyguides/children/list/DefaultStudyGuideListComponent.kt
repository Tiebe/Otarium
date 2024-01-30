package nl.tiebe.otarium.logic.default.home.children.elo.children.studyguides.children.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import dev.tiebe.magisterapi.api.studyguide.StudyGuideFlow
import dev.tiebe.magisterapi.response.studyguide.StudyGuide
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.logic.default.componentCoroutineScope
import nl.tiebe.otarium.logic.root.home.children.elo.children.studyguides.StudyGuidesChildComponent
import nl.tiebe.otarium.logic.root.home.children.elo.children.studyguides.children.list.StudyGuideListComponent
import nl.tiebe.otarium.utils.toFormattedString
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

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
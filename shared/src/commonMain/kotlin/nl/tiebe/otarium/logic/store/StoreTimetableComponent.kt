package nl.tiebe.otarium.logic.store

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.logic.default.componentCoroutineScope
import nl.tiebe.otarium.logic.root.home.children.timetable.TimetableRootComponent
import nl.tiebe.otarium.logic.root.home.children.timetable.children.timetable.TimetableComponent
import nl.tiebe.otarium.magister.AgendaItemWithAbsence
import nl.tiebe.otarium.utils.ui.getLocalizedString
import kotlin.math.floor

class StoreTimetableComponent(componentContext: ComponentContext, override val parentComponent: TimetableRootComponent): TimetableComponent, ComponentContext by componentContext {
    override val days = mutableListOf(
        getLocalizedString(MR.strings.monday),
        getLocalizedString(MR.strings.tuesday),
        getLocalizedString(MR.strings.wednesday),
        getLocalizedString(MR.strings.thursday),
        getLocalizedString(MR.strings.friday)
    ).run {
        if (Data.showWeekend) {
            this.add(getLocalizedString(MR.strings.saturday))
            this.add(getLocalizedString(MR.strings.sunday))
        }

        this.toList()
    }

    override val now: MutableValue<LocalDateTime> = MutableValue(Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam")))
    override val currentPage = MutableValue(500 + now.value.date.dayOfWeek.ordinal)

    override val timetable: MutableValue<List<AgendaItemWithAbsence>> = MutableValue(emptyList())

    override val selectedWeek = MutableValue(floor((currentPage.value - (amountOfDays / 2).toFloat()) / days.size).toInt())
    override val selectedDay = MutableValue(currentPage.value - (amountOfDays / 2) % 7)

    override val selectedWeekIndex = MutableValue(selectedWeek.value + amountOfWeeks / 2)

    override val isRefreshingTimetable = MutableValue(false)

    private val scope = componentCoroutineScope()

    override fun refreshTimetable(from: LocalDate, to: LocalDate) {
        scope.launch {
            isRefreshingTimetable.value = true

            delay(1000L)

            isRefreshingTimetable.value = false
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun scrollToPage(coroutineScope: CoroutineScope, page: Int, pagerState: PagerState) {
        coroutineScope.launch {
            pagerState.animateScrollToPage(page)
        }
        currentPage.value = page
    }


    init {
        selectedWeek.observe {
            refreshSelectedWeek()
        }

        scope.launch {
            while (true) {
                now.value = Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam"))

                delay(60_000)
            }
        }
    }

}
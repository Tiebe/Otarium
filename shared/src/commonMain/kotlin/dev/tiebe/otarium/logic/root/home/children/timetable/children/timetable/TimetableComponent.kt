package dev.tiebe.otarium.logic.root.home.children.timetable.children.timetable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.tiebe.otarium.MR
import dev.tiebe.otarium.magister.AgendaItemWithAbsence
import dev.tiebe.otarium.utils.ui.getLocalizedString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.math.floor

val days = listOf(
    getLocalizedString(MR.strings.monday),
    getLocalizedString(MR.strings.tuesday),
    getLocalizedString(MR.strings.wednesday),
    getLocalizedString(MR.strings.thursday),
    getLocalizedString(MR.strings.friday),
    getLocalizedString(MR.strings.saturday),
    getLocalizedString(MR.strings.sunday)
)

interface TimetableComponent {
    val now: Value<LocalDateTime>
    val firstDayOfWeek get() = now.value.date.minus(now.value.date.dayOfWeek.ordinal, DateTimeUnit.DAY)
    val amountOfDays get() = 1000
    val amountOfWeeks get() = amountOfDays / 5

    // The index of the day in the timetable that is today (in dayPagerState days, so day 500 is monday this week)
    val todayIndex get() = amountOfDays / 2 + now.value.dayOfWeek.ordinal

    // The selected day from 0-6 (monday-sunday)
    val selectedDay: Value<Int>

    val currentPage: Value<Int>

    val timetable: Value<List<AgendaItemWithAbsence>>

    // The index of the week in the timetable that of the day that is currently selected (so the current week would be 0)
    val selectedWeek: Value<Int>

    // The index of the week in the timetable that of the day that is currently selected (in weekPagerState weeks, so week 100 is this week)
    val selectedWeekIndex: Value<Int>

    //TODO: remove this variable (by returning Future or MutableValue from refreshTimetable)
    val isRefreshingTimetable: Value<Boolean>

    fun changeDay(day: Int) {
        (currentPage as MutableValue).value = day

        (selectedDay as MutableValue).value = currentPage.value - (amountOfDays / 2) % 7

        if (selectedWeek.value != floor((day - (amountOfDays / 2).toFloat()) / days.size).toInt()) {
            (selectedWeek as MutableValue).value = floor((day - (amountOfDays / 2).toFloat()) / days.size).toInt()
            (selectedWeekIndex as MutableValue).value = selectedWeek.value + amountOfWeeks / 2
        }
    }

    fun refreshTimetable(from: LocalDate, to: LocalDate)

    fun refreshSelectedWeek() {
        refreshTimetable(
            firstDayOfWeek.plus(
                selectedWeek.value * 7,
                DateTimeUnit.DAY
            ),
            firstDayOfWeek.plus(
                selectedWeek.value * 7,
                DateTimeUnit.DAY
            ).plus(days.size, DateTimeUnit.DAY)
        )
    }

    fun getTimetableForWeek(timetable: List<AgendaItemWithAbsence>, startOfWeekDate: LocalDate): List<AgendaItemWithAbsence> {
        return timetable.filter {
            val startTime =
                it.agendaItem.start.substring(0, 26).toLocalDateTime()

            startTime.date in startOfWeekDate..startOfWeekDate.plus(
                6,
                DateTimeUnit.DAY
            )
        }
    }

    fun openTimeTableItem(item: AgendaItemWithAbsence)

    fun closeItemPopup()

    @OptIn(ExperimentalFoundationApi::class)
    fun scrollToPage(coroutineScope: CoroutineScope, page: Int, pagerState: PagerState) {
        coroutineScope.launch {
            pagerState.animateScrollToPage(page)
        }
        (currentPage as MutableValue).value = page
    }
}

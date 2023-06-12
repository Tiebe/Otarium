package dev.tiebe.otarium.logic.default.home.children.timetable.children.timetable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.backhandler.BackCallback
import dev.tiebe.magisterapi.response.general.year.agenda.AgendaItem
import dev.tiebe.magisterapi.utils.MagisterException
import dev.tiebe.otarium.Data
import dev.tiebe.otarium.MR
import dev.tiebe.otarium.logic.default.componentCoroutineScope
import dev.tiebe.otarium.logic.root.home.children.timetable.TimetableRootComponent
import dev.tiebe.otarium.logic.root.home.children.timetable.children.timetable.TimetableComponent
import dev.tiebe.otarium.magister.AgendaItemWithAbsence
import dev.tiebe.otarium.magister.MagisterAccount
import dev.tiebe.otarium.magister.getAbsences
import dev.tiebe.otarium.magister.getMagisterAgenda
import dev.tiebe.otarium.utils.ui.getLocalizedString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
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

class DefaultTimetableComponent(
    componentContext: ComponentContext,
    val navigate: (TimetableRootComponent.Config) -> Unit,
    val back: MutableValue<BackCallback>,
): TimetableComponent, ComponentContext by componentContext {
    override val now: MutableValue<LocalDateTime> = MutableValue(Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam")))
    override val currentPage = MutableValue(500 + now.value.date.dayOfWeek.ordinal)

    override val timetable: MutableValue<List<AgendaItemWithAbsence>> = MutableValue(emptyList())

    override val selectedWeek = MutableValue(floor((currentPage.value - (amountOfDays / 2).toFloat()) / days.size).toInt())
    override val selectedDay = MutableValue(currentPage.value - (amountOfDays / 2) % 7)

    override val selectedWeekIndex = MutableValue(selectedWeek.value + amountOfWeeks / 2)

    override val isRefreshingTimetable = MutableValue(false)

    private val scope = componentCoroutineScope()

    override fun refreshTimetable(from: LocalDate, to: LocalDate) {
        val account: MagisterAccount = Data.selectedAccount

        scope.launch {
            isRefreshingTimetable.value = true
            try {
                val timeTableWeek = getAbsences(
                    account.accountId,
                    account.tenantUrl,
                    account.tokens.accessToken,
                    "${from.year}-${from.month}-${from.dayOfMonth}",
                    "${to.year}-${to.month}-${to.dayOfMonth}",
                    getMagisterAgenda(
                        account.accountId,
                        account.tenantUrl,
                        account.tokens.accessToken,
                        from,
                        to,
                        if (Data.showCancelledLessons) AgendaItem.Companion.Status.NONE else AgendaItem.Companion.Status.SCHEDULED_AUTOMATICALLY
                    )
                )

                if (selectedWeek.value == 0) {
                    account.agenda = timeTableWeek
                }

                timeTableWeek.forEach {
                    if (timetable.value.find { item -> item.agendaItem.id == it.agendaItem.id } == null) {
                        timetable.value = timetable.value + it
                    } else {
                        timetable.value = timetable.value.map { item ->
                            if (item.agendaItem.id == it.agendaItem.id) {
                                it
                            } else {
                                item
                            }
                        }
                    }
                }
            } catch (e: MagisterException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            isRefreshingTimetable.value = false
        }
    }

    override fun openTimeTableItem(item: AgendaItemWithAbsence) {
        navigate(TimetableRootComponent.Config.TimetablePopup(item.agendaItem.id))
    }

    override fun closeItemPopup() {
        back.value.onBack()
    }


    @OptIn(ExperimentalFoundationApi::class)
    override fun scrollToPage(coroutineScope: CoroutineScope, page: Int, pagerState: PagerState) {
        coroutineScope.launch {
            pagerState.animateScrollToPage(page)
        }
        currentPage.value = page
    }


    init {
        selectedWeek.subscribe {
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
package nl.tiebe.otarium.ui.home.timetable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import dev.tiebe.magisterapi.utils.MagisterException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.magister.AgendaItemWithAbsence
import nl.tiebe.otarium.magister.MagisterAccount
import nl.tiebe.otarium.magister.getAbsences
import nl.tiebe.otarium.magister.getMagisterAgenda
import nl.tiebe.otarium.ui.home.HomeComponent
import nl.tiebe.otarium.ui.home.MenuItemComponent
import nl.tiebe.otarium.ui.root.componentCoroutineScope
import nl.tiebe.otarium.utils.ui.getLocalizedString
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

interface TimetableComponent : MenuItemComponent {
    val now: Value<LocalDateTime>
    val firstDayOfWeek get() = now.value.date.minus(now.value.date.dayOfWeek.ordinal, DateTimeUnit.DAY)
    val amountOfDays get() = 1000

    val currentPage: Value<Int>

    val timetable: Value<List<AgendaItemWithAbsence>>

    val openedTimetableItem: Value<Pair<Boolean, AgendaItemWithAbsence?>>

    val selectedWeek: Value<Int>

    val isRefreshingTimetable: Value<Boolean>

    fun changeDay(day: Int) {
        (currentPage as MutableValue).value = day

        if (selectedWeek.value != floor((day - (amountOfDays / 2).toFloat()) / days.size).toInt())
            (selectedWeek as MutableValue).value = floor((day - (amountOfDays / 2).toFloat()) / days.size).toInt()
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

    val backCallbackOpenItem: BackCallback

    fun openTimeTableItem(item: AgendaItemWithAbsence) {
        backCallbackOpenItem.isEnabled = true

        (openedTimetableItem as MutableValue).value = true to item
    }

    fun closeItemPopup() {
        (openedTimetableItem as MutableValue).value = false to openedTimetableItem.value.second
        backCallbackOpenItem.isEnabled = false
    }

    @OptIn(ExperimentalFoundationApi::class)
    fun scrollToPage(coroutineScope: CoroutineScope, page: Int, pagerState: PagerState) {
        coroutineScope.launch {
            pagerState.animateScrollToPage(page)
        }
        (currentPage as MutableValue).value = page
    }
}

class DefaultTimetableComponent(
    componentContext: ComponentContext,
    navigate: (menuItem: HomeComponent.MenuItem) -> Unit
): TimetableComponent, ComponentContext by componentContext {
    override val now: MutableValue<LocalDateTime> = MutableValue(Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam")))
    override val currentPage = MutableValue(500 + now.value.date.dayOfWeek.ordinal)

    override val timetable: MutableValue<List<AgendaItemWithAbsence>> = MutableValue(emptyList())
    override val openedTimetableItem: MutableValue<Pair<Boolean, AgendaItemWithAbsence?>> = MutableValue(false to null)

    override val selectedWeek = MutableValue(floor((currentPage.value - (amountOfDays / 2).toFloat()) / days.size).toInt())

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
                        to
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

    override val backCallbackOpenItem: BackCallback = BackCallback(false) {
        closeItemPopup()
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun scrollToPage(coroutineScope: CoroutineScope, page: Int, pagerState: PagerState) {
        coroutineScope.launch {
            pagerState.animateScrollToPage(page)
        }
        currentPage.value = page
    }


    init {
        backHandler.register(backCallbackOpenItem)

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
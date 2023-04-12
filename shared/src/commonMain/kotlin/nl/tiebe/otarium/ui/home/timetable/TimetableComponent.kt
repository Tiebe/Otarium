package nl.tiebe.otarium.ui.home.timetable

import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
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

val timesShown = 8..17
val dpPerHour = 80.dp

interface TimetableComponent : MenuItemComponent {
    val now: Value<LocalDateTime>
    val firstDayOfWeek get() = now.value.date.minus(now.value.date.dayOfWeek.ordinal, DateTimeUnit.DAY)
    val amountOfDays get() = 1000

    val currentPage: Value<Int>

    val days: List<String>

    val timetable: Value<List<AgendaItemWithAbsence>>
    val account: MagisterAccount

    val openedTimetableItem: Value<Pair<Boolean, AgendaItemWithAbsence?>>

    val selectedWeek: Value<Int>

    val isRefreshingTimetable: Value<Boolean>

    fun getWeekFromPage(page: Int): Int = if (page >= 0) {
        page / days.size
    } else {
        floor((page / days.size.toFloat())).toInt()
    }

    fun getDateStartOfWeek(page: Int) = now.value.date.minus(
            now.value.date.dayOfWeek.ordinal,
            DateTimeUnit.DAY
        ) // first day of week
            .plus(getWeekFromPage(page) * 7, DateTimeUnit.DAY) // add weeks to get to selected week
            .plus(
                page - (getWeekFromPage(page) * days.size),
                DateTimeUnit.DAY
            ) // add days to get to selected day

    fun getTimeFromTop(page: Int) = getDateStartOfWeek(page).atStartOfDayIn(TimeZone.of("Europe/Amsterdam")).toEpochMilliseconds() + (timesShown.first() * 60 * 60 * 1000)

    fun changeDay(day: Int)

    fun refreshTimetable(from: LocalDate, to: LocalDate)

    fun getTotalAmountOfOverlaps(item: AgendaItemWithAbsence, items: List<AgendaItemWithAbsence>): Pair<List<AgendaItemWithAbsence>, Int> {
        val overlaps = getAmountOfOverlaps(item, items)

        var childOverlaps = 0

        for (overlap in overlaps) {
            childOverlaps += getTotalAmountOfOverlaps(overlap, overlaps).second
        }

        return overlaps to overlaps.size + childOverlaps
    }

    fun getAmountOfOverlaps(item: AgendaItemWithAbsence,
                            items: List<AgendaItemWithAbsence>
    ): List<AgendaItemWithAbsence> {
        return items.filter {
            it != item &&
                    item.overlaps(it)
        }
    }

    fun AgendaItemWithAbsence.overlaps(item: AgendaItemWithAbsence): Boolean {
        //check if this item overlaps with the given item
        val startTime = this.agendaItem.start.substring(0, 26).toLocalDateTime().toInstant(TimeZone.of("Europe/Amsterdam")).epochSeconds + 1
        val endTime = this.agendaItem.einde.substring(0, 26).toLocalDateTime().toInstant(TimeZone.of("Europe/Amsterdam")).epochSeconds - 1

        val startTime2 = item.agendaItem.start.substring(0, 26).toLocalDateTime().toInstant(TimeZone.of("Europe/Amsterdam")).epochSeconds + 1
        val endTime2 = item.agendaItem.einde.substring(0, 26).toLocalDateTime().toInstant(TimeZone.of("Europe/Amsterdam")).epochSeconds - 1

        return startTime in startTime2..endTime2 || endTime in startTime2..endTime2
    }

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

    @OptIn(ExperimentalPagerApi::class)
    fun scrollToPage(coroutineScope: CoroutineScope, page: Int, pagerState: PagerState)
}

class DefaultTimetableComponent(
    componentContext: ComponentContext,
    navigate: (menuItem: HomeComponent.MenuItem) -> Unit
): TimetableComponent, ComponentContext by componentContext {
    override val now: MutableValue<LocalDateTime> = MutableValue(Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam")))
    override val currentPage = MutableValue(500 + now.value.date.dayOfWeek.ordinal)

    override val days = listOf(
        getLocalizedString(MR.strings.monday),
        getLocalizedString(MR.strings.tuesday),
        getLocalizedString(MR.strings.wednesday),
        getLocalizedString(MR.strings.thursday),
        getLocalizedString(MR.strings.friday),
        getLocalizedString(MR.strings.saturday),
        getLocalizedString(MR.strings.sunday)
    )

    override val timetable: MutableValue<List<AgendaItemWithAbsence>> = MutableValue(emptyList())
    override val account: MagisterAccount = Data.selectedAccount
    override val openedTimetableItem: MutableValue<Pair<Boolean, AgendaItemWithAbsence?>> = MutableValue(false to null)

    override val selectedWeek = MutableValue(floor((currentPage.value - (amountOfDays / 2).toFloat()) / days.size).toInt())

    override val isRefreshingTimetable = MutableValue(false)

    override fun changeDay(day: Int) {
        currentPage.value = day

        if (selectedWeek.value != floor((day - (amountOfDays / 2).toFloat()) / days.size).toInt())
            selectedWeek.value = floor((day - (amountOfDays / 2).toFloat()) / days.size).toInt()
    }

    private val scope = componentCoroutineScope()

    override fun refreshTimetable(from: LocalDate, to: LocalDate) {
        scope.launch {
            isRefreshingTimetable.value = true
            try {
                println("Refreshing agenda for week $selectedWeek")

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
                    println("Saving agenda for current week")
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

    private fun closeItemPopup() {
        openedTimetableItem.value = false to openedTimetableItem.value.second
        backCallbackOpenItem.isEnabled = false
    }

    @OptIn(ExperimentalPagerApi::class)
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
package nl.tiebe.otarium.ui.home.timetable

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.utils.MagisterException
import kotlinx.datetime.*
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.magister.*
import nl.tiebe.otarium.ui.home.HomeComponent
import nl.tiebe.otarium.ui.home.MenuItemComponent
import nl.tiebe.otarium.utils.ui.getLocalizedString
import kotlin.math.floor

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

    fun changeDay(day: Int)

    suspend fun refreshTimetable(
        account: MagisterAccount,
        start: LocalDate,
        totalDays: Int,
        selectedWeek: Int
    ): List<List<AgendaItemWithAbsence>>?

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
}

class DefaultTimetableComponent(
    componentContext: ComponentContext,
    navigate: (menuItem: HomeComponent.MenuItem) -> Unit
): TimetableComponent, ComponentContext by componentContext {
    override val now: Value<LocalDateTime> = MutableValue(Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam")))
    override val currentPage = MutableValue(500 + now.value.date.dayOfWeek.ordinal)

    override val days =  listOf(
        getLocalizedString(MR.strings.monday),
        getLocalizedString(MR.strings.tuesday),
        getLocalizedString(MR.strings.wednesday),
        getLocalizedString(MR.strings.thursday),
        getLocalizedString(MR.strings.friday),
        getLocalizedString(MR.strings.saturday),
        getLocalizedString(MR.strings.sunday)
    )

    override val timetable: Value<List<AgendaItemWithAbsence>> = MutableValue(emptyList())
    override val account: MagisterAccount = Data.selectedAccount
    override val openedTimetableItem: Value<Pair<Boolean, AgendaItemWithAbsence?>> = MutableValue(false to null)

    override val selectedWeek = MutableValue(floor((currentPage.value - (amountOfDays / 2).toFloat()) / days.size).toInt())

    override fun changeDay(day: Int) {
        currentPage.value = day

        selectedWeek.value = floor((currentPage.value - (amountOfDays / 2).toFloat()) / days.size).toInt()
    }

    override suspend fun refreshTimetable(
        account: MagisterAccount,
        start: LocalDate,
        totalDays: Int,
        selectedWeek: Int
    ): List<List<AgendaItemWithAbsence>>? {
        try {
            println("Refreshing agenda for week $selectedWeek")

            val end = start.plus(totalDays, DateTimeUnit.DAY)

            val timeTableWeek = getAbsences(
                account.accountId,
                account.tenantUrl,
                account.tokens.accessToken,
                "${start.year}-${start.month}-${start.dayOfMonth}",
                "${end.year}-${end.month}-${end.dayOfMonth}",
                getMagisterAgenda(
                    account.accountId,
                    account.tenantUrl,
                    account.tokens.accessToken,
                    start,
                    end
                )
            )

            if (selectedWeek == 0) {
                println("Saving agenda for current week")
                account.agenda = timeTableWeek
            }

            return (0..totalDays).map { timeTableWeek.getAgendaForDay(it) }
        } catch (e: MagisterException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}

/*
    val loadedAgendas = remember {
        mutableStateMapOf(
            Pair(100, days.indices.map {
                savedAgenda.getAgendaForDay(it)
            })
        )
    }
* */


//todo: update agenda when switching weeks
/*
val newAgenda =
            component.refreshTimetable(component.account, component.firstDayOfSelectedWeek, component.days.size - 1, component.selectedWeek)
                ?: loadedAgendas[selectedWeek.value + 100]
 */

//todo: refresh function
/*
                scope.launch {
                    it.isRefreshing = true
                    loadedAgendas[selectedWeek.value + 100] = account.refreshAgenda(
                        firstDayOfSelectedWeek.value,
                        days.size - 1,
                        selectedWeek.value
                    ) ?: loadedAgendas[selectedWeek.value + 100] ?: listOf()
                    it.isRefreshing = false
                }
 */

//todo: update now variable
/*
    DisposableEffect(Unit) {
        GlobalScope.launch {
            while (true) {
                now = Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam"))

                val minutes = ((now.hour - 8) * 60) + now.minute

                if (minutes < 0) {
                    timeLinePosition.value = 0.dp
                } else {
                    timeLinePosition.value = minutes / 60f * dpPerHour
                }

                delay(60_000)
            }
        }

        onDispose {}
    }
 */
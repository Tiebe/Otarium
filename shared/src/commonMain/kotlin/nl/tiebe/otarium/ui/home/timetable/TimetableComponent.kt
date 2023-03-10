package nl.tiebe.otarium.ui.home.timetable

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.utils.MagisterException
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

    fun changeDay(day: Int)

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

    override val timetable: MutableValue<List<AgendaItemWithAbsence>> = MutableValue(emptyList())
    override val account: MagisterAccount = Data.selectedAccount
    override val openedTimetableItem: Value<Pair<Boolean, AgendaItemWithAbsence?>> = MutableValue(false to null)

    override val selectedWeek = MutableValue(floor((currentPage.value - (amountOfDays / 2).toFloat()) / days.size).toInt())

    override val isRefreshingTimetable = MutableValue(false)

    override fun changeDay(day: Int) {
        currentPage.value = day

        selectedWeek.value = floor((currentPage.value - (amountOfDays / 2).toFloat()) / days.size).toInt()
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
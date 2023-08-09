package nl.tiebe.otarium.logic.magister.home.children.timetable

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.response.general.year.absence.Absence
import dev.tiebe.magisterapi.response.general.year.agenda.AgendaItem
import dev.tiebe.magisterapi.utils.MagisterException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import nl.tiebe.otarium.logic.componentCoroutineScope
import nl.tiebe.otarium.logic.data.Data
import nl.tiebe.otarium.logic.home.children.timetable.TimetableComponent
import nl.tiebe.otarium.logic.magister.data.MagisterAccount
import nl.tiebe.otarium.logic.magister.data.getAbsences
import nl.tiebe.otarium.logic.magister.data.getMagisterAgenda
import kotlin.math.floor

val days = listOf(
    "Monday FIX",
    "Tuesday FIX",
    "Wednesday FIX",
    "Thursday FIX",
    "Friday FIX",
    "Saturday FIX",
    "Sunday FIX"
)

class DefaultTimetableComponent(
    componentContext: ComponentContext,
): TimetableComponent<AgendaItem, Absence>, ComponentContext by componentContext {
    override val navigation = StackNavigation<TimetableComponent.Config>()

    override val now: MutableValue<LocalDateTime> = MutableValue(Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam")))
    override val currentPage = MutableValue(500 + now.value.date.dayOfWeek.ordinal)

    override val timetable: MutableValue<List<TimetableComponent.FullTimetableItem<AgendaItem, Absence>>> = MutableValue(emptyList())
    override val selectedWeek = MutableValue(floor((currentPage.value - (amountOfDays / 2).toFloat()) / days.size).toInt())

    override val isRefreshingTimetable = MutableValue(false)

    private val scope = componentCoroutineScope()

    override fun getTimeTable(
        from: LocalDate,
        to: LocalDate
    ) {
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
                    if (timetable.value.find { item -> item.id == it.agendaItem.id } == null) {
                        timetable.value = timetable.value + TimetableComponent.FullTimetableItem(it.agendaItem.id, it.agendaItem, it.absence)
                    } else {
                        timetable.value = timetable.value.map { item ->
                            if (item.id == it.agendaItem.id) {
                                TimetableComponent.FullTimetableItem(it.agendaItem.id, it.agendaItem, it.absence)
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

    override fun getFilteredWeekTimeTable(startOfWeekDate: LocalDate): List<TimetableComponent.FullTimetableItem<AgendaItem, Absence>> =
        timetable.value.filter {
            val startTime =
                it.item.start.substring(0, 26).toLocalDateTime()

            startTime.date in startOfWeekDate..startOfWeekDate.plus(
                6,
                DateTimeUnit.DAY
            )
        }

    val childStack: Value<ChildStack<TimetableComponent.Config, Child>> =
        childStack(
            source = navigation,
            initialConfiguration = TimetableComponent.Config.Main,
            handleBackButton = false, // Pop the back stack on back button press
            childFactory = ::createChild,
        )

    private fun createChild(config: TimetableComponent.Config, @Suppress("UNUSED_PARAMETER") componentContext: ComponentContext): Child =
        when (config) {
            is TimetableComponent.Config.Main -> Child.TimetableChild(this)
            is TimetableComponent.Config.TimetablePopup -> Child.TimetablePopupChild(
                this,
                config.item
            )
        }

    sealed class Child {
        class TimetableChild(val component: TimetableComponent<AgendaItem, Absence>) : Child()
        class TimetablePopupChild(val component: TimetableComponent<AgendaItem, Absence>, val id: Int) : Child()
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
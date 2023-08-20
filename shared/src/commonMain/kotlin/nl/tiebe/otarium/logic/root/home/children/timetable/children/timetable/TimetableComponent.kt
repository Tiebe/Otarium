package nl.tiebe.otarium.logic.root.home.children.timetable.children.timetable

import com.arkivanov.decompose.value.MutableValue
import dev.tiebe.magisterapi.response.profileinfo.Contact
import kotlinx.datetime.*
import nl.tiebe.otarium.magister.AgendaItemWithAbsence

val days = listOf(
    DayOfWeek.MONDAY,
    DayOfWeek.TUESDAY,
    DayOfWeek.WEDNESDAY,
    DayOfWeek.THURSDAY,
    DayOfWeek.FRIDAY,
    DayOfWeek.SATURDAY,
    DayOfWeek.SUNDAY
)

interface TimetableComponent {
    val days: List<DayOfWeek>
        get() = listOf(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY
        )

    val timetable: MutableValue<List<AgendaItemWithAbsence>>

    suspend fun refreshTimetable(from: LocalDate, to: LocalDate)
    suspend fun getClassMembers(agendaItemWithAbsence: AgendaItemWithAbsence): List<Contact>

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

    fun openTimetableMemberPopup(item: AgendaItemWithAbsence)
}

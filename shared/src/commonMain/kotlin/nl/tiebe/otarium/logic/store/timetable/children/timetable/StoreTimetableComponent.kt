package nl.tiebe.otarium.logic.store.timetable.children.timetable

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import dev.tiebe.magisterapi.response.profileinfo.Contact
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDate
import nl.tiebe.otarium.logic.root.home.children.timetable.TimetableRootComponent
import nl.tiebe.otarium.logic.root.home.children.timetable.children.timetable.TimetableComponent
import nl.tiebe.otarium.magister.AgendaItemWithAbsence

class StoreTimetableComponent(
    componentContext: ComponentContext,
    val navigate: (TimetableRootComponent.Config) -> Unit,
    val back: () -> Unit,
): TimetableComponent, ComponentContext by componentContext {
    override val timetable: MutableValue<List<AgendaItemWithAbsence>> = MutableValue(emptyList())

    override suspend fun refreshTimetable(from: LocalDate, to: LocalDate) {
        delay(3000)
    }

    override suspend fun getClassMembers(agendaItemWithAbsence: AgendaItemWithAbsence): List<Contact> {
        return emptyList()
    }

    override fun openTimeTableItem(item: AgendaItemWithAbsence) {
        navigate(TimetableRootComponent.Config.TimetablePopup(item.agendaItem.id))
    }

    override fun closeItemPopup() {
        back()
    }

    override fun openTimetableMemberPopup(item: AgendaItemWithAbsence) {
        navigate(TimetableRootComponent.Config.TimetableMembers(item.agendaItem.id))
    }

    override fun openContactInfo(item: Contact) {
        navigate(TimetableRootComponent.Config.ContactInfo("${item.roepnaam ?: item.voorletters} ${item.tussenvoegsel?.plus(" ") ?: ""}${item.achternaam}"))
    }
}
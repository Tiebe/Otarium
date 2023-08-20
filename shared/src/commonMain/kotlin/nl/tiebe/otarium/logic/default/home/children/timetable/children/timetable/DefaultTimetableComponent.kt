package nl.tiebe.otarium.logic.default.home.children.timetable.children.timetable

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import dev.tiebe.magisterapi.api.account.ProfileInfoFlow
import dev.tiebe.magisterapi.response.general.year.agenda.AgendaItem
import dev.tiebe.magisterapi.response.profileinfo.Contact
import dev.tiebe.magisterapi.utils.MagisterException
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.logic.default.componentCoroutineScope
import nl.tiebe.otarium.logic.root.home.children.timetable.TimetableRootComponent
import nl.tiebe.otarium.logic.root.home.children.timetable.children.timetable.TimetableComponent
import nl.tiebe.otarium.magister.AgendaItemWithAbsence
import nl.tiebe.otarium.magister.MagisterAccount
import nl.tiebe.otarium.magister.getAbsences
import nl.tiebe.otarium.magister.getMagisterAgenda

class DefaultTimetableComponent(
    componentContext: ComponentContext,
    val navigate: (TimetableRootComponent.Config) -> Unit,
    val back: () -> Unit,
): TimetableComponent, ComponentContext by componentContext {
    override val timetable: MutableValue<List<AgendaItemWithAbsence>> = MutableValue(emptyList())

    private val scope = componentCoroutineScope()

    override suspend fun refreshTimetable(from: LocalDate, to: LocalDate) {
        val account: MagisterAccount = Data.selectedAccount

        scope.launch {
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
        }
    }

    override suspend fun getClassMembers(agendaItemWithAbsence: AgendaItemWithAbsence): List<Contact> {
        val className = agendaItemWithAbsence.agendaItem.description?.split(" - ")
        if (className == null || className.size < 3) {
            return emptyList()
        }

        return ProfileInfoFlow.getContacts(
            Data.selectedAccount.tenantUrl,
            Data.selectedAccount.tokens.accessToken,
            className[2]
        ).filter {
            it.type == "leerling"
        }.sortedBy { it.roepnaam ?: it.voorletters }
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
}
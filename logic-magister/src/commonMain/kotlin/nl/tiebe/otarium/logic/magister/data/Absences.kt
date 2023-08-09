package nl.tiebe.otarium.logic.magister.data

import dev.tiebe.magisterapi.api.absence.AbsenceFlow
import dev.tiebe.magisterapi.response.general.year.absence.Absence
import dev.tiebe.magisterapi.response.general.year.agenda.AgendaItem
import io.ktor.http.Url
import kotlinx.serialization.Serializable

suspend fun getAbsences(accountId: Int, tenantUrl: String, accessToken: String, start: String, end: String, loadedAgenda: List<AgendaItem>): List<AgendaItemWithAbsence> {
    val absence = AbsenceFlow.getAbsences(Url(tenantUrl), accessToken, accountId, start, end)
    val agendaWithAbsence = loadedAgenda.map { agenda ->
        val agendaAbsence = absence.firstOrNull { it.afspraakId == agenda.id }
        AgendaItemWithAbsence(agenda, agendaAbsence)
    }

    return agendaWithAbsence
}

@Serializable
data class AgendaItemWithAbsence(
    val agendaItem: AgendaItem,
    val absence: Absence?
)
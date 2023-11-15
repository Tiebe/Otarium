package nl.tiebe.otarium.magister

import dev.tiebe.magisterapi.api.absence.AbsenceFlow
import dev.tiebe.magisterapi.response.general.year.absence.Absence
import dev.tiebe.magisterapi.response.general.year.agenda.AgendaItem
import io.ktor.http.*
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
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
    val absence: Absence?,
    val col: Int = 0,
    val colSpan: Int = 1,
    val colTotal: Int = 1
) {
    val start: Instant get() = agendaItem.start.substring(0, 26).toLocalDateTime().toInstant(TimeZone.UTC)
    val end: Instant get() = agendaItem.einde.substring(0, 26).toLocalDateTime().toInstant(TimeZone.UTC)
}

private fun AgendaItemWithAbsence.overlapsWith(other: AgendaItemWithAbsence): Boolean {
    return start < other.end && end > other.start
}

private fun List<AgendaItemWithAbsence>.timesOverlapWith(event: AgendaItemWithAbsence): Boolean {
    return any { it.overlapsWith(event) }
}

fun arrangeEvents(events: List<AgendaItemWithAbsence>): List<AgendaItemWithAbsence> {
    val positionedEvents = mutableListOf<AgendaItemWithAbsence>()
    val groupEvents: MutableList<MutableList<AgendaItemWithAbsence>> = mutableListOf()

    fun resetGroup() {
        groupEvents.forEachIndexed { colIndex, col ->
            col.forEach { e ->
                positionedEvents.add(e.copy(col = colIndex, colTotal = groupEvents.size))
            }
        }
        groupEvents.clear()
    }

    events.forEach { event ->
        var firstFreeCol = -1
        var numFreeCol = 0
        for (i in 0 until groupEvents.size) {
            val col = groupEvents[i]
            if (col.timesOverlapWith(event)) {
                if (firstFreeCol < 0) continue else break
            }
            if (firstFreeCol < 0) firstFreeCol = i
            numFreeCol++
        }

        when {
            // Overlaps with all, add a new column
            firstFreeCol < 0 -> {
                groupEvents += mutableListOf(event)
                // Expand anything that spans into the previous column and doesn't overlap with this event
                for (ci in 0 until groupEvents.size - 1) {
                    val col = groupEvents[ci]
                    col.forEachIndexed { ei, e ->
                        if (ci + e.colSpan == groupEvents.size - 1 && !e.overlapsWith(event)) {
                            col[ei] = e.copy(colSpan = e.colSpan + 1)
                        }
                    }
                }
            }
            // No overlap with any, start a new group
            numFreeCol == groupEvents.size -> {
                resetGroup()
                groupEvents += mutableListOf(event)
            }
            // At least one column free, add to first free column and expand to as many as possible
            else -> {
                groupEvents[firstFreeCol] += event.copy(colSpan = numFreeCol)
            }
        }
    }
    resetGroup()
    return positionedEvents
}
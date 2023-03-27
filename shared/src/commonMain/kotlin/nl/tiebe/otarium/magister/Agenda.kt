package nl.tiebe.otarium.magister

import dev.tiebe.magisterapi.api.agenda.AgendaFlow.getAgenda
import dev.tiebe.magisterapi.response.general.year.agenda.AgendaItem
import io.ktor.http.*
import kotlinx.datetime.LocalDate

suspend fun getMagisterAgenda(
    accountId: Int,
    tenantUrl: String,
    accessToken: String,
    start: LocalDate,
    end: LocalDate
): List<AgendaItem> {
    return getAgenda(
        Url(tenantUrl),
        accessToken,
        accountId,
        "${start.year}-${start.monthNumber}-${start.dayOfMonth}",
        "${end.year}-${end.monthNumber}-${end.dayOfMonth}"
    )
}

fun List<AgendaItemWithAbsence>.getAgendaForDay(day: Int): List<AgendaItemWithAbsence> {
    return this.filter { item ->
        return@filter if (item.agendaItem.start.isNotEmpty()) {
            val date = item.agendaItem.start.substring(0, 10).split("-").map { it.toInt() }
            LocalDate(date[0], date[1], date[2]).dayOfWeek.ordinal == day
        } else { false }
    }
}
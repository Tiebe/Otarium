package nl.tiebe.otarium.magister

import io.ktor.http.*
import kotlinx.datetime.LocalDate
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.tiebe.magisterapi.api.agenda.AgendaFlow.getAgenda
import nl.tiebe.magisterapi.response.general.year.agenda.AgendaItem
import nl.tiebe.otarium.settings

suspend fun getMagisterAgenda(accountId: Int, tenantUrl: String, accessToken: String, start: LocalDate, end: LocalDate): List<AgendaItem> {
    val agenda = getAgenda(
        Url(tenantUrl),
        accessToken,
        accountId,
        "${start.year}-${start.monthNumber}-${start.dayOfMonth}",
        "${end.year}-${end.monthNumber}-${end.dayOfMonth}"
    )

    saveAgenda(agenda)
    return agenda
}

fun getSavedAgenda(): List<AgendaItem> {
    return settings.getStringOrNull("agenda")?.let { Json.decodeFromString(it) } ?: emptyList()
}

fun saveAgenda(agenda: List<AgendaItem>) {
    settings.putString("agenda", Json.encodeToString(agenda))
}


fun List<AgendaItem>.getAgendaForDay(day: Int): List<AgendaItem> {
    return this.filter { item ->
        return@filter if (item.start.isNotEmpty()) {
            val date = item.start.substring(0, 10).split("-").map { it.toInt() }
            LocalDate(date[0], date[1], date[2]).dayOfWeek.ordinal == day
        } else { false }
    }
}
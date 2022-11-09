package nl.tiebe.otarium.magister

import io.ktor.http.*
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
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
    val today = Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam")).date
    val dayOfWeek = today.dayOfWeek
    var check = false


    return this.filter { item ->
        return@filter if (item.start.isNotEmpty()) {
            val date = item.start.substring(0, 10).split("-").map { it.toInt() }
            if (check || today.toEpochDays() - LocalDate(date[0], date[1], date[2]).toEpochDays() <= dayOfWeek.ordinal) { check = true } else return listOf()
            LocalDate(date[0], date[1], date[2]).dayOfWeek.ordinal == day
        } else { false }
    }
}
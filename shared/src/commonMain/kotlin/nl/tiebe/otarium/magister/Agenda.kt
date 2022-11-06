package nl.tiebe.otarium.magister

import io.ktor.http.*
import kotlinx.datetime.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.tiebe.magisterapi.api.agenda.AgendaFlow.getAgenda
import nl.tiebe.magisterapi.response.general.year.agenda.AgendaItem
import nl.tiebe.otarium.settings

suspend fun getMagisterAgenda(accountId: Int, tenantUrl: String, accessToken: String): List<AgendaItem> {
    val today = Clock.System.now()
    val todayDate = today.toLocalDateTime(TimeZone.currentSystemDefault()).date

    val dateStartOfWeek = today.minus(todayDate.dayOfWeek.ordinal, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
    val dateEndOfWeek = dateStartOfWeek.plus(6, DateTimeUnit.DAY, TimeZone.currentSystemDefault())

    val start = dateStartOfWeek.toLocalDateTime(TimeZone.currentSystemDefault()).date
    val end = dateEndOfWeek.toLocalDateTime(TimeZone.currentSystemDefault()).date

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
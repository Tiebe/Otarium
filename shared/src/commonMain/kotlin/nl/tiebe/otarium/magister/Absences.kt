package nl.tiebe.otarium.magister

import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.tiebe.magisterapi.api.absence.AbsenceFlow
import nl.tiebe.magisterapi.response.general.year.absence.Absence
import nl.tiebe.otarium.settings

suspend fun getAbsences(accountId: Int, tenantUrl: String, accessToken: String, start: String, end: String): List<Absence> {
    val absence = AbsenceFlow.getAbsences(Url(tenantUrl), accessToken, accountId, start, end)

    saveAbsences(absence)
    return absence
}

fun getSavedAbsences(): List<Absence> {
    return settings.getStringOrNull("absences")?.let { Json.decodeFromString(it) } ?: emptyList()
}

fun saveAbsences(absences: List<Absence>) {
    settings.putString("absences", Json.encodeToString(absences))
}
package nl.tiebe.otarium.magister

import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.tiebe.magisterapi.api.grades.GradeFlow
import nl.tiebe.magisterapi.response.general.year.grades.RecentGrade
import nl.tiebe.otarium.settings

suspend fun getRecentGrades(accountId: Int, tenantUrl: String, accessToken: String): List<RecentGrade> {
    val grades = GradeFlow.getRecentGrades(Url(tenantUrl), accessToken, accountId, 30, 0)

    saveGrades(grades)
    return grades
}

fun getSavedGrades(): List<RecentGrade> {
    return settings.getStringOrNull("grades")?.let { Json.decodeFromString(it) } ?: emptyList()
}

fun saveGrades(grades: List<RecentGrade>) {
    settings.putString("grades", Json.encodeToString(grades))
}
package nl.tiebe.otarium.magister

import io.ktor.http.*
import kotlinx.serialization.Serializable
import nl.tiebe.magisterapi.api.grades.GradeFlow
import nl.tiebe.magisterapi.response.general.year.grades.Grade
import nl.tiebe.magisterapi.response.general.year.grades.GradeInfo
import nl.tiebe.magisterapi.response.general.year.grades.RecentGrade
import nl.tiebe.otarium.Data.Magister.Grades.saveGrades

suspend fun getRecentGrades(accountId: Int, tenantUrl: String, accessToken: String): List<RecentGrade> {
    val grades = GradeFlow.getRecentGrades(Url(tenantUrl), accessToken, accountId, 30, 0)

    saveGrades(grades)
    return grades
}

@Serializable
data class GradeWithGradeInfo(
    val grade: Grade,
    val gradeInfo: GradeInfo
)
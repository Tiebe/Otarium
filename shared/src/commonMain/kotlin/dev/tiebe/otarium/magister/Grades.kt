package dev.tiebe.otarium.magister

import dev.tiebe.magisterapi.api.general.GeneralFlow
import dev.tiebe.magisterapi.api.grades.GradeFlow
import dev.tiebe.magisterapi.response.general.year.grades.Grade
import dev.tiebe.magisterapi.response.general.year.grades.GradeColumn
import dev.tiebe.magisterapi.response.general.year.grades.GradeInfo
import dev.tiebe.magisterapi.response.general.year.grades.RecentGrade
import io.ktor.http.*
import kotlinx.serialization.Serializable
import dev.tiebe.otarium.utils.sendNotification

suspend fun MagisterAccount.getRecentGrades(amount: Int, skip: Int): List<RecentGrade> {
    val newGrades = GradeFlow.getRecentGrades(Url(tenantUrl), tokens.accessToken, accountId, amount, skip)

    grades = newGrades
    return newGrades
}

suspend fun MagisterAccount.refreshGrades(notification: (String, String) -> Unit = { title, message -> sendNotification(title, message) }): List<GradeWithGradeInfo> {
    val years = GeneralFlow.getYears(tenantUrl, tokens.accessToken, accountId)
    val grades = GradeFlow.getGrades(Url(tenantUrl), tokens.accessToken, accountId, years[0]).filter {
        it.gradeColumn.type == GradeColumn.Type.Grade ||
                it.gradeColumn.type == GradeColumn.Type.Text
    }

    val newFullGradeList: MutableList<GradeWithGradeInfo> = fullGradeList.toMutableList()

    newGrades@ for (grade in grades) {
        for (oldGrade in fullGradeList) {
            if (grade.id == oldGrade.grade.id) {
                if (grade.grade != oldGrade.grade.grade) {
                    try {
                        val gradeInfo = GradeFlow.getGradeInfo(
                            Url(tenantUrl),
                            tokens.accessToken,
                            accountId,
                            grade
                        )

                        newFullGradeList.remove(oldGrade)
                        newFullGradeList.add(GradeWithGradeInfo(grade, gradeInfo))

                        notification(
                            "Je cijfer is gewijzigd: ${grade.subject.description.trim()}",
                            "Je hebt nu een ${grade.grade?.trim()} voor ${gradeInfo.columnDescription?.trim()}"
                        )

                    } catch (e: Exception) { e.printStackTrace() }
                }
                continue@newGrades
            }
        }

        try {
            val gradeInfo = GradeFlow.getGradeInfo(
                Url(tenantUrl),
                tokens.accessToken,
                accountId,
                grade
            )
            newFullGradeList.add(GradeWithGradeInfo(grade, gradeInfo))

            if (fullGradeList.isNotEmpty()) {
                notification(
                    "Nieuw cijfer: ${grade.subject.description.trim()}",
                    "Je hebt een ${grade.grade?.trim()} voor ${gradeInfo.columnDescription?.trim()} gekregen."
                )
            }
        } catch (e: Exception) { e.printStackTrace() }
    }

    fullGradeList = newFullGradeList
    return fullGradeList
}

@Serializable
data class GradeWithGradeInfo(
    val grade: Grade,
    val gradeInfo: GradeInfo
)

@Serializable
data class ManualGrade(
    val name: String,
    val grade: String,
    val weight: Float,
    val subjectId: Int,
)
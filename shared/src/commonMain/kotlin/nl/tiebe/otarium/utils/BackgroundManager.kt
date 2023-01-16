package nl.tiebe.otarium.utils

import io.ktor.http.*
import nl.tiebe.magisterapi.api.account.LoginFlow
import nl.tiebe.magisterapi.api.general.GeneralFlow
import nl.tiebe.magisterapi.api.grades.GradeFlow
import nl.tiebe.magisterapi.response.general.year.grades.GradeColumn
import nl.tiebe.otarium.Data.Magister.Grades.getSavedFullGradeList
import nl.tiebe.otarium.Data.Magister.Grades.saveFullGradeList
import nl.tiebe.otarium.magister.GradeWithGradeInfo
import nl.tiebe.otarium.magister.MagisterAccount
import nl.tiebe.otarium.magister.Tokens

expect fun reloadTokensBackground()

expect fun refreshGradesBackground()

suspend fun refreshTokens(): MagisterAccount? {
    val savedTokens = Tokens.getSavedMagisterTokens()
    val newTokens = LoginFlow.refreshToken(savedTokens?.tokens?.refreshToken ?: return null)

    Tokens.saveMagisterTokens(MagisterAccount(savedTokens.accountId, savedTokens.tenantUrl, newTokens))
    return MagisterAccount(savedTokens.accountId, savedTokens.tenantUrl, newTokens)
}

suspend fun refreshGrades(): List<GradeWithGradeInfo>? {
    val tokens = Tokens.getMagisterTokens() ?: return null
    val oldGradeList = getSavedFullGradeList()

    val years = GeneralFlow.getYears(tokens.tenantUrl, tokens.tokens.accessToken, tokens.accountId)
    val grades = GradeFlow.getGrades(Url(tokens.tenantUrl), tokens.tokens.accessToken, tokens.accountId, years[0]).filter {
        it.gradeColumn.type == GradeColumn.Type.Grade ||
                it.gradeColumn.type == GradeColumn.Type.Text
    }

    val fullGradeList: MutableList<GradeWithGradeInfo> = oldGradeList.toMutableList()

    newGrades@ for (grade in grades) {
        for (oldGrade in oldGradeList) {
            if (grade.id == oldGrade.grade.id) {
                if (grade.grade != oldGrade.grade.grade) {
                    try {
                        val gradeInfo = GradeFlow.getGradeInfo(
                            Url(tokens.tenantUrl),
                            tokens.tokens.accessToken,
                            tokens.accountId,
                            grade
                        )

                        fullGradeList.remove(oldGrade)
                        fullGradeList.add(GradeWithGradeInfo(grade, gradeInfo))

                        sendNotification(
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
                Url(tokens.tenantUrl),
                tokens.tokens.accessToken,
                tokens.accountId,
                grade
            )
            fullGradeList.add(GradeWithGradeInfo(grade, gradeInfo))
            println(oldGradeList)

            if (oldGradeList.isNotEmpty()) {
                sendNotification(
                    "Nieuw cijfer: ${grade.subject.description.trim()}",
                    "Je hebt een ${grade.grade?.trim()} voor ${gradeInfo.columnDescription?.trim()} gekregen."
                )
            }
        } catch (e: Exception) { e.printStackTrace() }
    }

    saveFullGradeList(fullGradeList)
    return fullGradeList
}

expect fun sendNotification(title: String, message: String)
package nl.tiebe.otarium.utils

import io.ktor.client.call.*
import io.ktor.http.*
import nl.tiebe.magisterapi.api.account.LoginFlow
import nl.tiebe.magisterapi.api.general.GeneralFlow
import nl.tiebe.magisterapi.api.grades.GradeFlow
import nl.tiebe.otarium.MAGISTER_TOKENS_URL
import nl.tiebe.otarium.getSavedFullGradeList
import nl.tiebe.otarium.magister.Tokens
import nl.tiebe.otarium.saveFullGradeList
import nl.tiebe.otarium.useServer
import nl.tiebe.otarium.utils.server.MagisterTokenResponse
import nl.tiebe.otarium.utils.server.ServerGrade

expect fun reloadTokensBackground()

expect fun refreshGradesBackground()

suspend fun refreshTokens(accessToken: String?): MagisterTokenResponse? {
    if (useServer()) {
        val newTokens: MagisterTokenResponse = requestGET(
            MAGISTER_TOKENS_URL,
            hashMapOf(),
            accessToken
        ).body()

        Tokens.saveMagisterTokens(newTokens)
        return newTokens
    } else {
        val savedTokens = Tokens.getSavedMagisterTokens()
        val newTokens = LoginFlow.refreshToken(savedTokens?.tokens?.refreshToken ?: return null)

        Tokens.saveMagisterTokens(MagisterTokenResponse(savedTokens.accountId, savedTokens.tenantUrl, newTokens))
        return MagisterTokenResponse(savedTokens.accountId, savedTokens.tenantUrl, newTokens)
    }
}

suspend fun refreshGrades(): List<ServerGrade>? {
    val tokens = Tokens.getMagisterTokens(null) ?: return null
    val oldGradeList = getSavedFullGradeList()

    val years = GeneralFlow.getYears(tokens.tenantUrl, tokens.tokens.accessToken, tokens.accountId)
    val grades = GradeFlow.getGrades(Url(tokens.tenantUrl), tokens.tokens.accessToken, tokens.accountId, years[0])

    val fullGradeList: MutableList<ServerGrade> = oldGradeList.toMutableList()

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
                        fullGradeList.add(ServerGrade("", grade, gradeInfo))

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
            fullGradeList.add(ServerGrade("", grade, gradeInfo))
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
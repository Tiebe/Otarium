package nl.tiebe.otarium.magister

import dev.tiebe.magisterapi.api.account.LoginFlow
import dev.tiebe.magisterapi.response.TokenResponse
import dev.tiebe.magisterapi.response.general.year.grades.RecentGrade
import dev.tiebe.magisterapi.response.profileinfo.ProfileInfo
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.settings

@Serializable
data class MagisterAccount(
    val accountId: Int,
    val profileInfo: ProfileInfo,
    val tenantUrl: String,
    private val savedTokens: TokenResponse
) {
    var agenda: List<AgendaItemWithAbsence>
        get() = settings.getStringOrNull("agenda-$accountId")?.let { Json.decodeFromString(it) } ?: emptyList()
        set(value) = settings.putString("agenda-$accountId", Json.encodeToString(value))

    var grades: List<RecentGrade>
        get() = settings.getStringOrNull("grades-$accountId")?.let { Json.decodeFromString(it) } ?: emptyList()
        set(value) = settings.putString("grades-$accountId", Json.encodeToString(value))

    var fullGradeList: List<GradeWithGradeInfo>
        get() = settings.getStringOrNull("full_grade_list-$accountId")?.let { Json.decodeFromString(it) } ?: emptyList()
        set(value) = settings.putString("full_grade_list-$accountId", Json.encodeToString(value))

    var tokens: TokenResponse
        get() = runBlocking {
            if (!savedTokens.expiresAt.isAfterNow) {
                savedTokens
            } else refreshTokens().tokens
        }

        set(value) {
            settings.putString("tokens-$accountId", Json.encodeToString(value))
        }

    private val Long.isAfterNow: Boolean
        get() = Clock.System.now().toEpochMilliseconds()/1000 + 20 > this

    suspend fun refreshTokens(): MagisterAccount {
        val newTokens = LoginFlow.refreshToken(savedTokens.refreshToken)

        Data.accounts = Data.accounts.map {
            if (it.accountId == accountId) MagisterAccount(it.accountId, it.profileInfo, it.tenantUrl, newTokens)
            else it
        }

        return MagisterAccount(accountId, profileInfo, tenantUrl, newTokens)
    }

}
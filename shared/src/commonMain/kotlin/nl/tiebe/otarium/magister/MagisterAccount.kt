package nl.tiebe.otarium.magister

import dev.tiebe.magisterapi.api.account.LoginFlow
import dev.tiebe.magisterapi.response.TokenResponse
import dev.tiebe.magisterapi.response.general.year.grades.RecentGrade
import dev.tiebe.magisterapi.response.messages.MessageFolder
import dev.tiebe.magisterapi.response.profileinfo.ProfileInfo
import dev.tiebe.magisterapi.utils.MagisterException
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.tiebe.otarium.settings

@Serializable
data class MagisterAccount(
    val accountId: Int,
    val profileInfo: ProfileInfo,
    val profileImage: ByteArray,
    val tenantUrl: String,
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

    var messageFolders: List<MessageFolder>
        get() = settings.getStringOrNull("message_folders-$accountId")?.let { Json.decodeFromString(it) } ?: emptyList()
        set(value) = settings.putString("message_folders-$accountId", Json.encodeToString(value))

    var tokens: TokenResponse
        get() = runBlocking {
            val savedTokens: TokenResponse = settings.getStringOrNull("tokens-$accountId")?.let { Json.decodeFromString(it) } ?: throw IllegalStateException("No tokens found!")

            if (!savedTokens.expiresAt.isAfterNow) {
                savedTokens
            } else refreshTokens()
        }

        set(value) {
            settings.putString("tokens-$accountId", Json.encodeToString(value))
        }

    suspend fun refreshTokens(): TokenResponse {
        try {
            val savedTokens: TokenResponse =
                settings.getStringOrNull("tokens-$accountId")?.let { Json.decodeFromString(it) }
                    ?: throw IllegalStateException("No tokens found!")
            val newTokens = LoginFlow.refreshToken(savedTokens.refreshToken)

            tokens = newTokens

            return newTokens
        } catch (e: MagisterException) {
            if (e.statusCode == HttpStatusCode.Unauthorized || e.statusCode == HttpStatusCode.Forbidden) {
               //todo: show popup


            }
        } catch (_: Exception) {}
        return settings.getStringOrNull("tokens-$accountId")?.let { Json.decodeFromString(it) }
            ?: throw IllegalStateException("No tokens found!")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as MagisterAccount

        if (accountId != other.accountId) return false
        if (tenantUrl != other.tenantUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = accountId
        result = 31 * result + tenantUrl.hashCode()
        return result
    }

}

val Long.isAfterNow: Boolean
    get() = Clock.System.now().toEpochMilliseconds()/1000 + 20 > this
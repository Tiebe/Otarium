package nl.tiebe.otarium.logic.magister.data.wrapper

import dev.tiebe.magisterapi.api.account.LoginFlow
import dev.tiebe.magisterapi.api.messages.MessageFlow
import dev.tiebe.magisterapi.response.TokenResponse
import dev.tiebe.magisterapi.response.messages.Message
import dev.tiebe.magisterapi.response.messages.MessageFolder
import dev.tiebe.magisterapi.response.profileinfo.ProfileInfo
import dev.tiebe.magisterapi.utils.MagisterException
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.tiebe.otarium.logic.data.Data
import nl.tiebe.otarium.logic.data.wrapper.Account
import nl.tiebe.otarium.logic.data.wrapper.FullTimetableItem
import nl.tiebe.otarium.logic.magister.data.GradeWithGradeInfo
import nl.tiebe.otarium.logic.magister.data.MagisterAccount
import nl.tiebe.otarium.logic.magister.data.wrapper.messages.MessageItem

class Account(
    override val id: Int,
    val profileInfo: ProfileInfo,
    val profileImage: ByteArray,
    val tenantUrl: String,
) : Account {
    override var agenda: List<FullTimetableItem>
        get() = Data.settings.getStringOrNull("agenda-$id")?.let { Json.decodeFromString(it) } ?: emptyList()
        set(value) = Data.settings.putString("agenda-$id", Json.encodeToString(value))

    override var grades: List<RecentGradeWrapper>
        get() = Data.settings.getStringOrNull("grades-$id")?.let { Json.decodeFromString(it) } ?: emptyList()
        set(value) = Data.settings.putString("grades-$id", Json.encodeToString(value))

    override var messages: List<MessageItemWrapper>
        get() = Data.settings.getStringOrNull("messages-$id")?.let { Json.decodeFromString(it) } ?: emptyList()
        set(value) = Data.settings.putString("messages-$id", Json.encodeToString(value))

    override var fullGradeList: List<GradeWithGradeInfo>
        get() = Data.settings.getStringOrNull("full_grade_list-$id")?.let { Json.decodeFromString(it) } ?: emptyList()
        set(value) = Data.settings.putString("full_grade_list-$id", Json.encodeToString(value))

    var messageFolders: List<MessageFolder>
        get() = Data.settings.getStringOrNull("message_folders-$id")?.let { Json.decodeFromString(it) } ?: emptyList()
        set(value) = Data.settings.putString("message_folders-$id", Json.encodeToString(value))

    var tokens: TokenResponse
        get() = runBlocking {
            val savedTokens: TokenResponse = Data.settings.getStringOrNull("tokens-$id")?.let { Json.decodeFromString(it) } ?: throw IllegalStateException("No tokens found!")

            if (!savedTokens.expiresAt.isAfterNow) {
                savedTokens
            } else refreshTokens()
        }

        set(value) {
            Data.settings.putString("tokens-$id", Json.encodeToString(value))
        }

    suspend fun refreshFolders(): List<MessageFolder> {
        val folders =
            MessageFlow.getAllFolders(
                Url(tenantUrl),
                tokens.accessToken
            )

        unreadMessages.value = folders.sumOf { it.unreadCount }
        messageFolders = folders
        return folders
    }

    suspend fun refreshTokens(): TokenResponse {
        try {
            val savedTokens: TokenResponse =
                Data.settings.getStringOrNull("tokens-$id")?.let { Json.decodeFromString(it) }
                    ?: throw IllegalStateException("No tokens found!")
            val newTokens = LoginFlow.refreshToken(savedTokens.refreshToken)

            tokens = newTokens

            return newTokens
        } catch (e: MagisterException) {
            if (e.statusCode == HttpStatusCode.Unauthorized || e.statusCode == HttpStatusCode.Forbidden) {
                //todo: show popup


            }
        } catch (_: Exception) {}
        return Data.settings.getStringOrNull("tokens-$id")?.let { Json.decodeFromString(it) }
            ?: throw IllegalStateException("No tokens found!")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as MagisterAccount

        if (id != other.accountId) return false
        if (tenantUrl != other.tenantUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + tenantUrl.hashCode()
        return result
    }

}

val Long.isAfterNow: Boolean
    get() = Clock.System.now().toEpochMilliseconds()/1000 + 20 > this
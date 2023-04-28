package nl.tiebe.otarium.utils

import io.ktor.utils.io.*
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

expect fun copyToClipboard(text: String)

expect fun getClipboardText(): String

expect fun openUrl(url: String)

expect fun getDownloadFileLocation(id: String, fileName: String): ByteWriteChannel

expect fun openFileFromCache(id: String, fileName: String)

fun LocalDateTime.toFormattedString(): String {
    val dateTime = this.toInstant(TimeZone.UTC).toLocalDateTime(TimeZone.of("Europe/Amsterdam"))

    return "${dateTime.dayOfMonth}-${dateTime.monthNumber}-${dateTime.year} ${dateTime.hour}:${dateTime.minute}:${dateTime.second}"
}
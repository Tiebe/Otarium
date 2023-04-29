package nl.tiebe.otarium.utils

import io.ktor.utils.io.*
import kotlinx.datetime.*

expect fun copyToClipboard(text: String)

expect fun getClipboardText(): String

expect fun openUrl(url: String)

expect fun getDownloadFileLocation(id: String, fileName: String): ByteWriteChannel

expect fun openFileFromCache(id: String, fileName: String)

fun LocalDateTime.toFormattedString(): String {
    val dateTime = this.toInstant(TimeZone.UTC)

    return dateTime.toFormattedString()
}

fun LocalDateTime.toFormattedStringDate(): String {
    val dateTime = this.toInstant(TimeZone.UTC)

    return dateTime.toFormattedStringDate()
}

fun LocalDateTime.toFormattedStringTime(): String {
    val dateTime = this.toInstant(TimeZone.UTC)

    return dateTime.toFormattedStringTime()
}

fun Instant.toFormattedString(): String {
    return this.toFormattedStringDate() + " " + this.toFormattedStringTime()
}

fun Instant.toFormattedStringDate(): String {
    val dateTime = this.toLocalDateTime(TimeZone.of("Europe/Amsterdam"))

    return "${dateTime.dayOfMonth.toFormattedString()}-${dateTime.monthNumber.toFormattedString()}-${dateTime.year.toFormattedString()}"
}

fun Instant.toFormattedStringTime(): String {
    val dateTime = this.toLocalDateTime(TimeZone.of("Europe/Amsterdam"))

    return "${dateTime.hour.toFormattedString()}:${dateTime.minute.toFormattedString()}:${dateTime.second.toFormattedString()}"
}

fun Int.toFormattedString(): String {
    return if (this < 10) "0$this" else "$this"
}
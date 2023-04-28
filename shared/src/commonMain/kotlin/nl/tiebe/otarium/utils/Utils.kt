package nl.tiebe.otarium.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

expect fun copyToClipboard(text: String)

expect fun getClipboardText(): String

expect fun openUrl(url: String)

public fun LocalDateTime.toFormattedString(): String {
    val dateTime = this.toInstant(TimeZone.UTC).toLocalDateTime(TimeZone.of("Europe/Amsterdam"))

    return "${dateTime.dayOfMonth}-${dateTime.monthNumber}-${dateTime.year} ${dateTime.hour}:${dateTime.minute}:${dateTime.second}"
}
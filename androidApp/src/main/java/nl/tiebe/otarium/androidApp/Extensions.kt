package nl.tiebe.otarium.androidApp

import dev.tiebe.magisterapi.response.general.year.agenda.AgendaItem
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

val Float.millisToHours: Float
    get() = this / 1000 / 60 / 60

val String.magisterDateToInstant: Instant
    get() = this.substring(0, 26).toLocalDateTime().toInstant(TimeZone.UTC)

val AgendaItem.Companion.Status.isCancelled: Boolean
    get() = this == AgendaItem.Companion.Status.CANCELED_MANUALLY || this == AgendaItem.Companion.Status.CANCELED_AUTOMATICALLY
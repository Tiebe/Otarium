package nl.tiebe.otarium.logic.default.home.children.timetable.children.timetable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import dev.tiebe.magisterapi.response.general.year.agenda.AgendaItem
import dev.tiebe.magisterapi.utils.MagisterException
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.logic.default.componentCoroutineScope
import nl.tiebe.otarium.logic.root.home.children.timetable.TimetableRootComponent
import nl.tiebe.otarium.logic.root.home.children.timetable.children.timetable.TimetableComponent
import nl.tiebe.otarium.magister.AgendaItemWithAbsence
import nl.tiebe.otarium.magister.MagisterAccount
import nl.tiebe.otarium.magister.getAbsences
import nl.tiebe.otarium.magister.getMagisterAgenda
import nl.tiebe.otarium.utils.openFileFromCache
import nl.tiebe.otarium.utils.requestGET
import nl.tiebe.otarium.utils.ui.getLocalizedString
import nl.tiebe.otarium.utils.writeFile
import kotlin.math.floor


class DefaultTimetableComponent(
    componentContext: ComponentContext,
    override val parentComponent: TimetableRootComponent,
): TimetableComponent, ComponentContext by componentContext {
    override val days = mutableListOf(
        getLocalizedString(MR.strings.monday),
        getLocalizedString(MR.strings.tuesday),
        getLocalizedString(MR.strings.wednesday),
        getLocalizedString(MR.strings.thursday),
        getLocalizedString(MR.strings.friday)
    ).run {
        if (Data.showWeekend) {
            this.add(getLocalizedString(MR.strings.saturday))
            this.add(getLocalizedString(MR.strings.sunday))
        }

        this.toList()
    }

    override val now: MutableValue<LocalDateTime> = MutableValue(Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam")))
    override val currentPage = MutableValue(500 + now.value.date.dayOfWeek.ordinal)

    override val timetable: MutableValue<List<AgendaItemWithAbsence>> = MutableValue(emptyList())

    override val selectedWeek = MutableValue(floor((currentPage.value - (amountOfDays / 2).toFloat()) / days.size).toInt())
    override val selectedDay = MutableValue(currentPage.value - (amountOfDays / 2) % 7)

    override val selectedWeekIndex = MutableValue(selectedWeek.value + amountOfWeeks / 2)

    override val isRefreshingTimetable = MutableValue(false)

    private val scope = componentCoroutineScope()

    override fun refreshTimetable(from: LocalDate, to: LocalDate) {
        val account: MagisterAccount = Data.selectedAccount

        scope.launch {
            isRefreshingTimetable.value = true
            try {
                val timeTableWeek = getAbsences(
                    account.accountId,
                    account.tenantUrl,
                    account.tokens.accessToken,
                    "${from.year}-${from.month}-${from.dayOfMonth}",
                    "${to.year}-${to.month}-${to.dayOfMonth}",
                    getMagisterAgenda(
                        account.accountId,
                        account.tenantUrl,
                        account.tokens.accessToken,
                        from,
                        to,
                        if (Data.showCancelledLessons) AgendaItem.Companion.Status.NONE else AgendaItem.Companion.Status.SCHEDULED_AUTOMATICALLY
                    )
                )

                if (selectedWeek.value == 0) {
                    account.agenda = timeTableWeek
                }

                timeTableWeek.forEach {
                    if (timetable.value.find { item -> item.agendaItem.id == it.agendaItem.id } == null) {
                        timetable.value = timetable.value + it
                    } else {
                        timetable.value = timetable.value.map { item ->
                            if (item.agendaItem.id == it.agendaItem.id) {
                                it
                            } else {
                                item
                            }
                        }
                    }
                }
            } catch (e: MagisterException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            isRefreshingTimetable.value = false
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun scrollToPage(coroutineScope: CoroutineScope, page: Int, pagerState: PagerState) {
        coroutineScope.launch {
            pagerState.animateScrollToPage(page)
        }
        currentPage.value = page
    }

    override val attachmentDownloadProgress: MutableValue<Map<Int, Float>> = MutableValue(mapOf())


    override fun downloadAttachment(attachment: AgendaItem.Companion.Attachment) {
        scope.launch {
            val response = requestGET(
                //TODO: probably not correct
                URLBuilder(Data.selectedAccount.tenantUrl).appendEncodedPathSegments(attachment.url!!).build(),
                accessToken = Data.selectedAccount.tokens.accessToken,
                onDownload = { bytesSentTotal, contentLength ->
                    attachmentDownloadProgress.value += Pair(
                        attachment.id,
                        bytesSentTotal.toFloat() / contentLength.toFloat()
                    )
                }
            ).readBytes()

            writeFile(attachment.id.toString(), attachment.naam, response)
            openFileFromCache(attachment.id.toString(), attachment.naam)

            attachmentDownloadProgress.value = attachmentDownloadProgress.value.toMutableMap().also {
                it.remove(attachment.id)
            }
        }
    }

    init {
        selectedWeek.observe {
            refreshSelectedWeek()
        }

        scope.launch {
            while (true) {
                now.value = Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam"))

                delay(60_000)
            }
        }
    }

}
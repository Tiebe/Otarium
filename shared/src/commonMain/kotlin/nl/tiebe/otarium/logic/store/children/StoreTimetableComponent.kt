package nl.tiebe.otarium.logic.store.children

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.logic.default.home.children.timetable.children.timetable.DefaultTimetableComponent
import nl.tiebe.otarium.magister.AgendaItemWithAbsence
import nl.tiebe.otarium.ui.home.MenuItemComponent
import nl.tiebe.otarium.ui.home.timetable.main.TimetableComponent
import nl.tiebe.otarium.ui.home.timetable.main.days
import nl.tiebe.otarium.ui.root.componentCoroutineScope
import nl.tiebe.otarium.utils.ui.getText
import kotlin.math.floor

class StoreTimetableComponent(
    componentContext: ComponentContext
): DefaultTimetableComponent(componentContext) {
    override val now: MutableValue<LocalDateTime> =
        MutableValue(Clock.System.now()
            .minus(Clock.System.now() - LocalDateTime(2023, 5, 8, 0, 0, 0).toInstant(TimeZone.of("Europe/Amsterdam")))
            .toLocalDateTime(TimeZone.of("Europe/Amsterdam"))
        )
    override val currentPage = MutableValue(500 + now.value.date.dayOfWeek.ordinal)

    override val timetable: MutableValue<List<AgendaItemWithAbsence>> = MutableValue(emptyList())
    override val selectedWeek =
        MutableValue(floor((currentPage.value - (amountOfDays / 2).toFloat()) / days.size).toInt())

    override val isRefreshingTimetable = MutableValue(false)

    private val scope = componentCoroutineScope()

    override fun refreshTimetable(from: LocalDate, to: LocalDate) {
        scope.launch {
            isRefreshingTimetable.value = true
            delay(1000)
            timetable.value = Json.decodeFromString(getText(MR.files.timetable))

            isRefreshingTimetable.value = false
        }
    }

    override fun openTimeTableItem(item: AgendaItemWithAbsence) {
        //TODO("Not yet implemented")
    }

    override fun closeItemPopup() {
        //TODO("Not yet implemented")
    }

    init {
        selectedWeek.subscribe {
            refreshSelectedWeek()
        }

        scope.launch {
            while (true) {
                now.value = Clock.System.now()
                    .minus(Clock.System.now() - LocalDateTime(2023, 5, 8, 0, 0, 0).toInstant(TimeZone.of("Europe/Amsterdam")))
                    .toLocalDateTime(TimeZone.of("Europe/Amsterdam"))

                delay(60_000)
            }
        }
    }
}
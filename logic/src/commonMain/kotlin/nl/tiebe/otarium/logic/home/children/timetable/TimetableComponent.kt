package nl.tiebe.otarium.logic.home.children.timetable

import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus

/**
 * Interface for the implementation of the backend for the timetable UI.
 */
interface TimetableComponent<TimetableItem, ItemInformation> {
    val navigation: StackNavigation<Config>


    // Constants
    /** The amount of days supported. The user can't swipe further than this amount. */
    val amountOfDays get() = 700
    /** The amount of weeks. This is simply the amount of days divided by 7. */
    val amountOfWeeks get() = amountOfDays / 7

    /** The current date and time. This value should be updated at least once a minute, since this time will be displayed in the UI. */
    val now: MutableValue<LocalDateTime>

    /** Calculation variable for getting the first day of the week. */
    val firstDayOfWeek get() = now.value.date.minus(now.value.date.dayOfWeek.ordinal, DateTimeUnit.DAY)


    /** Calculation variable for getting the current day of the week from 0 (monday) to 6 (friday). */
    val selectedDay get() = currentPage.value - (amountOfDays / 2) % 7

    /** The currently selected page, as an index. */
    val currentPage: MutableValue<Int>

    /** The list of timetable items loaded from the server. */
    val timetable: Value<List<Pair<TimetableItem, ItemInformation>>>

    // The index of the week in the timetable that of the day that is currently selected (so the current week would be 0)
    val selectedWeek: Value<Int>

    //TODO: remove this variable (by returning Future or MutableValue from refreshTimetable)
    val isRefreshingTimetable: Value<Boolean>

    /**
     * Set this day as the selected day.
     *
     * @param dayIndex The index of the day to select.
     */
    fun selectDay(dayIndex: Int) {
        currentPage.value = dayIndex
    }

    /**
     * Retrieve a timetable from the server.
     *
     * @param from The starting date.
     * @param to The ending date.
     *
     * @return A list of timetable items.
     */
    fun getTimeTable(from: LocalDate, to: LocalDate): List<FullTimetableItem<TimetableItem, ItemInformation>>

    /**
     * Refresh the currently selected week.
     *
     * @return A list of timetable items.
     */
    fun refreshSelectedWeek() =
        getTimeTable(
            firstDayOfWeek.plus( // first day of the week
                selectedWeek.value * 7,
                DateTimeUnit.DAY
            ),
            firstDayOfWeek.plus( // last day of the week (first day + 6 days)
                selectedWeek.value * 7,
                DateTimeUnit.DAY
            ).plus(6, DateTimeUnit.DAY)
        )

    /**
     * Get the timetable items for a specific week. Should filter the currently stored timetable, and then return a list with the items for that week.
     *
     * @param startOfWeekDate The monday of the specified week, represented as a LocalDate object.
     * @return A list of the timetable items, with their extra information.
     */
    fun getFilteredWeekTimeTable(startOfWeekDate: LocalDate): List<FullTimetableItem<TimetableItem, ItemInformation>>

    /**
     * Open the an extra information popup with info about the timetable item. For example: start time, end time, teacher, homework.
     *
     * @param item The timetable item to show information about
     */
    fun openTimetableItemDetails(item: FullTimetableItem<TimetableItem, ItemInformation>) {
        navigation.push(Config.TimetablePopup(item.id))
    }

    /**
     * Close the currently opened extra information popup.
     */
    fun closeTimetableItemDetails() {
        navigation.pop()
    }

    /**
     * A wrapper class for a pair of the item and the extra information associated with this item.
     *
     * @param item The timetable item
     * @param extraInformation The extra information associated with this item
     */
    data class FullTimetableItem<TimetableItem, ItemInformation>(val id: Int, val item: TimetableItem, val extraInformation: ItemInformation)

    /**
     * The navigation config objects.
     */
    sealed class Config : Parcelable {
        /**
         * The main timetable screen.
         */
        @Parcelize
        data object Main : Config()

        /**
         * The popup for extra information
         */
        @Parcelize
        data class TimetablePopup(val item: Int) : Config()
    }
}


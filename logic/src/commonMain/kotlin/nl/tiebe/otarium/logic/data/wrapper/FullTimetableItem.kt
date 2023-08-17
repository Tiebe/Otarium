package nl.tiebe.otarium.logic.data.wrapper

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

/**
 * A wrapper class for a pair of the item and the extra information associated with this item.
 *
 * @param item The timetable item
 * @param information The extra information associated with this item
 */
@Parcelize
data class FullTimetableItem(val item: TimetableItem, val information: TimetableItemInformation?): Parcelable

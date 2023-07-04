package nl.tiebe.otarium.logic.root.home

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import dev.icerock.moko.resources.StringResource
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.utils.OtariumIcons
import nl.tiebe.otarium.utils.otariumicons.Bottombar
import nl.tiebe.otarium.utils.otariumicons.bottombar.*

@Parcelize
sealed class MenuItems(val resourceId: StringResource, val icon: @Composable () -> Unit, val iconSelected: @Composable () -> Unit):
    Parcelable {
    object Timetable: MenuItems(
        MR.strings.agendaItem,
        { Icon(OtariumIcons.Bottombar.CalendarTodayOutline, "Timetable") },
        { Icon(OtariumIcons.Bottombar.CalendarTodayFilled, "Timetable") },
    )

    object Grades: MenuItems(
        MR.strings.gradesItem,
        { Icon(OtariumIcons.Bottombar.Box10Outline, "Grades") },
        { Icon(OtariumIcons.Bottombar.Box10Filled, "Grades") },
    )

    object Averages: MenuItems(
        MR.strings.average,
        { Icon(OtariumIcons.Bottombar.ChartOutline, "Averages") },
        { Icon(OtariumIcons.Bottombar.ChartFilled, "Averages") },
    )

    object Messages: MenuItems(
        MR.strings.messagesItem,
        { Icon(OtariumIcons.Bottombar.EmailOutline, "Messages") },
        { Icon(OtariumIcons.Bottombar.EmailFilled, "Messages") },
    )

    object ELO: MenuItems(
        MR.strings.eloItem,
        { Icon(OtariumIcons.Bottombar.BookOpenOutline, "ELO") },
        { Icon(OtariumIcons.Bottombar.BookOpenFilled, "ELO") },
    )

    object Settings: MenuItems(
        MR.strings.settingsItem,
        { Icon(OtariumIcons.Bottombar.CogOutline, "Settings") },
        { Icon(OtariumIcons.Bottombar.CogFilled, "Settings") },
    )

    object Debug: MenuItems(
        MR.strings.settingsItem,
        { Icon(OtariumIcons.Bottombar.Box10Outline, "Debug") },
        { Icon(OtariumIcons.Bottombar.Box10Filled, "Debug") },
    )
}
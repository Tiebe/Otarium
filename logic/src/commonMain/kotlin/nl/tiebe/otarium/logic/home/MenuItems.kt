package nl.tiebe.otarium.logic.home

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import dev.icerock.moko.resources.StringResource
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.utils.OtariumIcons
import nl.tiebe.otarium.utils.otariumicons.Bottombar
import nl.tiebe.otarium.utils.otariumicons.bottombar.BookOpenFilled
import nl.tiebe.otarium.utils.otariumicons.bottombar.BookOpenOutline
import nl.tiebe.otarium.utils.otariumicons.bottombar.Box10Filled
import nl.tiebe.otarium.utils.otariumicons.bottombar.Box10Outline
import nl.tiebe.otarium.utils.otariumicons.bottombar.CalendarTodayFilled
import nl.tiebe.otarium.utils.otariumicons.bottombar.CalendarTodayOutline
import nl.tiebe.otarium.utils.otariumicons.bottombar.CogFilled
import nl.tiebe.otarium.utils.otariumicons.bottombar.CogOutline
import nl.tiebe.otarium.utils.otariumicons.bottombar.EmailFilled
import nl.tiebe.otarium.utils.otariumicons.bottombar.EmailOutline

@Parcelize
sealed class MenuItems(val resourceId: StringResource, val icon: @Composable () -> Unit, val iconSelected: @Composable () -> Unit):
    Parcelable {
    data object Timetable: MenuItems(
        MR.strings.agendaItem,
        { Icon(OtariumIcons.Bottombar.CalendarTodayOutline, "Timetable") },
        { Icon(OtariumIcons.Bottombar.CalendarTodayFilled, "Timetable") },
    )

    data object Grades: MenuItems(
        MR.strings.gradesItem,
        { Icon(OtariumIcons.Bottombar.Box10Outline, "Grades") },
        { Icon(OtariumIcons.Bottombar.Box10Filled, "Grades") },
    )

    data object Messages: MenuItems(
        MR.strings.messagesItem,
        { Icon(OtariumIcons.Bottombar.EmailOutline, "Messages") },
        { Icon(OtariumIcons.Bottombar.EmailFilled, "Messages") },
    )

    data object ELO: MenuItems(
        MR.strings.eloItem,
        { Icon(OtariumIcons.Bottombar.BookOpenOutline, "ELO") },
        { Icon(OtariumIcons.Bottombar.BookOpenFilled, "ELO") },
    )

    data object Settings: MenuItems(
        MR.strings.settingsItem,
        { Icon(OtariumIcons.Bottombar.CogOutline, "Settings") },
        { Icon(OtariumIcons.Bottombar.CogFilled, "Settings") },
    )

    data object Debug: MenuItems(
        MR.strings.settingsItem,
        { Icon(OtariumIcons.Bottombar.Box10Outline, "Debug") },
        { Icon(OtariumIcons.Bottombar.Box10Filled, "Debug") },
    )
}
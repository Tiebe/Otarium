package nl.tiebe.otarium.logic.root.home

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import kotlinx.serialization.Serializable
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.utils.OtariumIcons
import nl.tiebe.otarium.utils.calculateAverageGrade
import nl.tiebe.otarium.utils.otariumicons.Bottombar
import nl.tiebe.otarium.utils.otariumicons.bottombar.*
import nl.tiebe.otarium.utils.otariumicons.bottombar.grades.*
import nl.tiebe.otarium.utils.ui.getLocalizedString
import kotlin.math.roundToInt

val unreadMessages = MutableValue(Data.selectedAccount.messageFolders.sumOf { it.unreadCount })
val filledIcons = listOf(
    OtariumIcons.Bottombar.Grades.Box1Filled,
    OtariumIcons.Bottombar.Grades.Box2Filled,
    OtariumIcons.Bottombar.Grades.Box3Filled,
    OtariumIcons.Bottombar.Grades.Box4Filled,
    OtariumIcons.Bottombar.Grades.Box5Filled,
    OtariumIcons.Bottombar.Grades.Box6Filled,
    OtariumIcons.Bottombar.Grades.Box7Filled,
    OtariumIcons.Bottombar.Grades.Box8Filled,
    OtariumIcons.Bottombar.Grades.Box9Filled,
    OtariumIcons.Bottombar.Grades.Box10Filled,
)

val outlineIcons = listOf(
    OtariumIcons.Bottombar.Grades.Box1Outline,
    OtariumIcons.Bottombar.Grades.Box2Outline,
    OtariumIcons.Bottombar.Grades.Box3Outline,
    OtariumIcons.Bottombar.Grades.Box4Outline,
    OtariumIcons.Bottombar.Grades.Box5Outline,
    OtariumIcons.Bottombar.Grades.Box6Outline,
    OtariumIcons.Bottombar.Grades.Box7Outline,
    OtariumIcons.Bottombar.Grades.Box8Outline,
    OtariumIcons.Bottombar.Grades.Box9Outline,
    OtariumIcons.Bottombar.Grades.Box10Outline,
)

@Serializable
sealed class MenuItems(val text: () -> String, val icon: @Composable () -> Unit, val iconSelected: @Composable () -> Unit) {
    @Serializable
    data object Timetable: MenuItems(
        { getLocalizedString(MR.strings.agendaItem) },
        { Icon(OtariumIcons.Bottombar.CalendarTodayOutline, "Timetable") },
        { Icon(OtariumIcons.Bottombar.CalendarTodayFilled, "Timetable") },
    )

    @Serializable
    data object Grades: MenuItems(
        { getLocalizedString(MR.strings.gradesItem) },
        {
            val average = calculateAverageGrade(Data.selectedAccount.fullGradeList).roundToInt()
            if (average < 1 || average > 10) Icon(OtariumIcons.Bottombar.Grades.Box10Outline, "Grades")
            else Icon(outlineIcons[average - 1], "Grades")
        },
        {
            val average = calculateAverageGrade(Data.selectedAccount.fullGradeList).roundToInt()
            if (average < 1 || average > 10) Icon(OtariumIcons.Bottombar.Grades.Box10Filled, "Grades")
            else Icon(filledIcons[average - 1], "Grades")
        },
    )

    @Serializable
    data object Messages: MenuItems(
        { getLocalizedString(MR.strings.messagesItem) },
        { BadgedBox(badge = {
            if (unreadMessages.subscribeAsState().value > 0) {
                Badge(
                    content = { Text(unreadMessages.subscribeAsState().value.toString()) }
                )
            }
        }) { Icon(OtariumIcons.Bottombar.EmailOutline, "Messages") } },
        { BadgedBox(badge = {
            if (unreadMessages.subscribeAsState().value > 0) {
                Badge(
                    content = { Text(unreadMessages.subscribeAsState().value.toString()) }
                )
            }
        }) { Icon(OtariumIcons.Bottombar.EmailFilled, "Messages") } },
    )

    @Serializable
    data object ELO: MenuItems(
        { getLocalizedString(MR.strings.eloItem) },
        { Icon(OtariumIcons.Bottombar.BookOpenOutline, "ELO") },
        { Icon(OtariumIcons.Bottombar.BookOpenFilled, "ELO") },
    )

    @Serializable
    data object Settings: MenuItems(
        { getLocalizedString(MR.strings.settingsItem) },
        { Icon(OtariumIcons.Bottombar.CogOutline, "Settings") },
        { Icon(OtariumIcons.Bottombar.CogFilled, "Settings") },
    )

    @Serializable
    data object Debug: MenuItems(
        { getLocalizedString(MR.strings.settingsItem) },
        { Icon(OtariumIcons.Bottombar.Box10Outline, "Debug") },
        { Icon(OtariumIcons.Bottombar.Box10Filled, "Debug") },
    )
}
package nl.tiebe.otarium.utils.otariumicons

import androidx.compose.ui.graphics.vector.ImageVector
import nl.tiebe.otarium.utils.OtariumIcons
import nl.tiebe.otarium.utils.otariumicons.bottombar.AllAssets
import nl.tiebe.otarium.utils.otariumicons.bottombar.BookOpenFilled
import nl.tiebe.otarium.utils.otariumicons.bottombar.BookOpenOutline
import nl.tiebe.otarium.utils.otariumicons.bottombar.CalendarTodayFilled
import nl.tiebe.otarium.utils.otariumicons.bottombar.CalendarTodayOutline
import nl.tiebe.otarium.utils.otariumicons.bottombar.ChartFilled
import nl.tiebe.otarium.utils.otariumicons.bottombar.ChartOutline
import nl.tiebe.otarium.utils.otariumicons.bottombar.CogFilled
import nl.tiebe.otarium.utils.otariumicons.bottombar.CogOutline
import nl.tiebe.otarium.utils.otariumicons.bottombar.EmailFilled
import nl.tiebe.otarium.utils.otariumicons.bottombar.EmailOutline
import nl.tiebe.otarium.utils.otariumicons.bottombar.Grades
import kotlin.collections.List as ____KtList

public object BottombarGroup

public val OtariumIcons.Bottombar: BottombarGroup
  get() = BottombarGroup

private var __AllAssets: ____KtList<ImageVector>? = null

public val BottombarGroup.AllAssets: ____KtList<ImageVector>
  get() {
    if (__AllAssets != null) {
      return __AllAssets!!
    }
    __AllAssets= Grades.AllAssets + listOf(BookOpenFilled, BookOpenOutline, CalendarTodayFilled,
        CalendarTodayOutline, ChartFilled, ChartOutline, CogFilled, CogOutline, EmailFilled,
        EmailOutline)
    return __AllAssets!!
  }

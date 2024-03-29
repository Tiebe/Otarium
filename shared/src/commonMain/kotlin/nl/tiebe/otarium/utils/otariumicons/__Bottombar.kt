package nl.tiebe.otarium.utils.otariumicons

import androidx.compose.ui.graphics.vector.ImageVector
import nl.tiebe.otarium.utils.OtariumIcons
import nl.tiebe.otarium.utils.otariumicons.bottombar.BookOpenFilled
import nl.tiebe.otarium.utils.otariumicons.bottombar.BookOpenOutline
import nl.tiebe.otarium.utils.otariumicons.bottombar.Box10Filled
import nl.tiebe.otarium.utils.otariumicons.bottombar.Box10Outline
import nl.tiebe.otarium.utils.otariumicons.bottombar.CalendarTodayFilled
import nl.tiebe.otarium.utils.otariumicons.bottombar.CalendarTodayOutline
import nl.tiebe.otarium.utils.otariumicons.bottombar.ChartFilled
import nl.tiebe.otarium.utils.otariumicons.bottombar.ChartOutline
import nl.tiebe.otarium.utils.otariumicons.bottombar.CogFilled
import nl.tiebe.otarium.utils.otariumicons.bottombar.CogOutline
import nl.tiebe.otarium.utils.otariumicons.bottombar.EmailFilled
import nl.tiebe.otarium.utils.otariumicons.bottombar.EmailOutline
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
    __AllAssets= listOf(CogOutline, Box10Filled, EmailFilled, BookOpenOutline, CalendarTodayFilled,
        EmailOutline, ChartFilled, CogFilled, ChartOutline, BookOpenFilled, CalendarTodayOutline,
        Box10Outline)
    return __AllAssets!!
  }

package dev.tiebe.otarium.utils.otariumicons

import androidx.compose.ui.graphics.vector.ImageVector
import dev.tiebe.otarium.utils.OtariumIcons
import dev.tiebe.otarium.utils.otariumicons.bottombar.BookOpenFilled
import dev.tiebe.otarium.utils.otariumicons.bottombar.BookOpenOutline
import dev.tiebe.otarium.utils.otariumicons.bottombar.Box10Filled
import dev.tiebe.otarium.utils.otariumicons.bottombar.Box10Outline
import dev.tiebe.otarium.utils.otariumicons.bottombar.CalendarTodayFilled
import dev.tiebe.otarium.utils.otariumicons.bottombar.CalendarTodayOutline
import dev.tiebe.otarium.utils.otariumicons.bottombar.CogFilled
import dev.tiebe.otarium.utils.otariumicons.bottombar.CogOutline
import dev.tiebe.otarium.utils.otariumicons.bottombar.EmailFilled
import dev.tiebe.otarium.utils.otariumicons.bottombar.EmailOutline
import kotlin.collections.List as ____KtList

public object BottombarGroup

public val OtariumIcons.Bottombar: BottombarGroup
  get() = BottombarGroup

private var __AllIcons: ____KtList<ImageVector>? = null

public val BottombarGroup.AllIcons: ____KtList<ImageVector>
  get() {
    if (__AllIcons != null) {
      return __AllIcons!!
    }
    __AllIcons= listOf(BookOpenFilled, BookOpenOutline, Box10Filled, Box10Outline,
        CalendarTodayFilled, CalendarTodayOutline, CogFilled, CogOutline, EmailFilled, EmailOutline)
    return __AllIcons!!
  }

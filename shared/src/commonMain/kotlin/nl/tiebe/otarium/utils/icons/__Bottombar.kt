package nl.tiebe.otarium.utils.icons

import androidx.compose.ui.graphics.vector.ImageVector
import nl.tiebe.otarium.utils.icons.Icons
import nl.tiebe.otarium.utils.icons.bottombar.Box10Filled
import nl.tiebe.otarium.utils.icons.bottombar.Box10Outline
import nl.tiebe.otarium.utils.icons.bottombar.CalendarTodayFilled
import nl.tiebe.otarium.utils.icons.bottombar.CalendarTodayOutline
import nl.tiebe.otarium.utils.icons.bottombar.CogFilled
import nl.tiebe.otarium.utils.icons.bottombar.CogOutline
import nl.tiebe.otarium.utils.icons.bottombar.EmailFilled
import nl.tiebe.otarium.utils.icons.bottombar.EmailOutline
import kotlin.collections.List as ____KtList

public object BottombarGroup

public val Icons.Bottombar: BottombarGroup
  get() = BottombarGroup

private var __AllIcons: ____KtList<ImageVector>? = null

public val BottombarGroup.AllIcons: ____KtList<ImageVector>
  get() {
    if (__AllIcons != null) {
      return __AllIcons!!
    }
    __AllIcons = listOf(Box10Filled, Box10Outline, CalendarTodayFilled, CalendarTodayOutline,
        CogFilled, CogOutline, EmailFilled, EmailOutline)
    return __AllIcons!!
  }

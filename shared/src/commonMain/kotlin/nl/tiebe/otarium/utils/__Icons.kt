package nl.tiebe.otarium.utils

import androidx.compose.ui.graphics.vector.ImageVector
import nl.tiebe.otarium.utils.icons.bottombar.BookOpenFilled
import nl.tiebe.otarium.utils.icons.bottombar.BookOpenOutline
import kotlin.collections.List as ____KtList

public object Icons

private var __AllIcons: ____KtList<ImageVector>? = null

public val Icons.AllIcons: ____KtList<ImageVector>
  get() {
    if (__AllIcons != null) {
      return __AllIcons!!
    }
    __AllIcons= listOf(BookOpenOutline, BookOpenFilled)
    return __AllIcons!!
  }

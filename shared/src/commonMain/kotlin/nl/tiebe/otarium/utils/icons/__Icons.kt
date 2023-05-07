package nl.tiebe.otarium.utils.icons

import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.collections.List as ____KtList

public object Icons

private var __AllIcons: ____KtList<ImageVector>? = null

public val Icons.AllIcons: ____KtList<ImageVector>
  get() {
    if (__AllIcons != null) {
      return __AllIcons!!
    }
    __AllIcons= Bottombar.AllIcons + Email.AllIcons + listOf(Advertisements, AdvertisementsOff,
        BugOutline, Folder)
    return __AllIcons!!
  }

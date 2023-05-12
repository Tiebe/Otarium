package nl.tiebe.otarium.utils

import androidx.compose.ui.graphics.vector.ImageVector
import nl.tiebe.otarium.utils.otariumicons.*
import kotlin.collections.List as ____KtList

public object OtariumIcons

private var __AllIcons: ____KtList<ImageVector>? = null

public val OtariumIcons.AllIcons: ____KtList<ImageVector>
  get() {
    if (__AllIcons != null) {
      return __AllIcons!!
    }
    __AllIcons= Bottombar.AllIcons + Email.AllIcons + listOf(Advertisements, AdvertisementsOff,
        BugOutline, Folder, ContentSave, Palette)
    return __AllIcons!!
  }

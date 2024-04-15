package nl.tiebe.otarium.utils

import androidx.compose.ui.graphics.vector.ImageVector
import nl.tiebe.otarium.utils.otariumicons.Advertisements
import nl.tiebe.otarium.utils.otariumicons.AdvertisementsOff
import nl.tiebe.otarium.utils.otariumicons.AllAssets
import nl.tiebe.otarium.utils.otariumicons.Bottombar
import nl.tiebe.otarium.utils.otariumicons.BugOutline
import nl.tiebe.otarium.utils.otariumicons.Cards
import nl.tiebe.otarium.utils.otariumicons.ContentSave
import nl.tiebe.otarium.utils.otariumicons.Down
import nl.tiebe.otarium.utils.otariumicons.Email
import nl.tiebe.otarium.utils.otariumicons.Folder
import nl.tiebe.otarium.utils.otariumicons.List
import nl.tiebe.otarium.utils.otariumicons.Palette
import kotlin.collections.List as ____KtList

public object OtariumIcons

private var __AllAssets: ____KtList<ImageVector>? = null

public val OtariumIcons.AllAssets: ____KtList<ImageVector>
  get() {
    if (__AllAssets != null) {
      return __AllAssets!!
    }
    __AllAssets= Bottombar.AllAssets + Email.AllAssets + listOf(Advertisements, AdvertisementsOff,
        BugOutline, Cards, ContentSave, Down, Folder, List, Palette)
    return __AllAssets!!
  }

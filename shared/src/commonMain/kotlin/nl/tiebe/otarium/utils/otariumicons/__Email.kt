package nl.tiebe.otarium.utils.otariumicons

import androidx.compose.ui.graphics.vector.ImageVector
import nl.tiebe.otarium.utils.OtariumIcons
import nl.tiebe.otarium.utils.otariumicons.email.Attachment
import nl.tiebe.otarium.utils.otariumicons.email.AttachmentOff
import nl.tiebe.otarium.utils.otariumicons.email.EmailAlert
import nl.tiebe.otarium.utils.otariumicons.email.EmailAlertOpen
import nl.tiebe.otarium.utils.otariumicons.email.EmailOpen
import kotlin.collections.List as ____KtList

public object EmailGroup

public val OtariumIcons.Email: EmailGroup
  get() = EmailGroup

private var __AllAssets: ____KtList<ImageVector>? = null

public val EmailGroup.AllAssets: ____KtList<ImageVector>
  get() {
    if (__AllAssets != null) {
      return __AllAssets!!
    }
    __AllAssets= listOf(Attachment, AttachmentOff, EmailAlert, EmailAlertOpen, EmailOpen)
    return __AllAssets!!
  }
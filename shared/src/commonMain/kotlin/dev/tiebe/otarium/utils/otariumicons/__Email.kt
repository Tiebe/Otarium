package dev.tiebe.otarium.utils.otariumicons

import androidx.compose.ui.graphics.vector.ImageVector
import dev.tiebe.otarium.utils.OtariumIcons
import dev.tiebe.otarium.utils.otariumicons.email.Attachment
import dev.tiebe.otarium.utils.otariumicons.email.AttachmentOff
import dev.tiebe.otarium.utils.otariumicons.email.EmailAlert
import dev.tiebe.otarium.utils.otariumicons.email.EmailAlertOpen
import dev.tiebe.otarium.utils.otariumicons.email.EmailOpen
import kotlin.collections.List as ____KtList

public object EmailGroup

public val OtariumIcons.Email: EmailGroup
  get() = EmailGroup

private var __AllIcons: ____KtList<ImageVector>? = null

public val EmailGroup.AllIcons: ____KtList<ImageVector>
  get() {
    if (__AllIcons != null) {
      return __AllIcons!!
    }
    __AllIcons= listOf(Attachment, AttachmentOff, EmailAlert, EmailAlertOpen, EmailOpen)
    return __AllIcons!!
  }

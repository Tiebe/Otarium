package nl.tiebe.otarium.utils.icons

import androidx.compose.ui.graphics.vector.ImageVector
import nl.tiebe.otarium.utils.icons.email.*
import kotlin.collections.List as ____KtList

public object EmailGroup

public val Icons.Email: EmailGroup
  get() = EmailGroup

private var __AllIcons: ____KtList<ImageVector>? = null

public val EmailGroup.AllIcons: ____KtList<ImageVector>
  get() {
    if (__AllIcons != null) {
      return __AllIcons!!
    }
    __AllIcons = listOf(Attachment, AttachmentOff, EmailAlert, EmailAlertOpen, EmailOpen)
    return __AllIcons!!
  }

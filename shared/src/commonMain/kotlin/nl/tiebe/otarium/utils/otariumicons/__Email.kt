package nl.tiebe.otarium.utils.otariumicons

import androidx.compose.ui.graphics.vector.ImageVector
import nl.tiebe.otarium.utils.OtariumIcons
import nl.tiebe.otarium.utils.otariumicons.email.Attachment
import nl.tiebe.otarium.utils.otariumicons.email.AttachmentOff
import nl.tiebe.otarium.utils.otariumicons.email.Delete
import nl.tiebe.otarium.utils.otariumicons.email.DeleteRestore
import nl.tiebe.otarium.utils.otariumicons.email.EmailAlert
import nl.tiebe.otarium.utils.otariumicons.email.EmailAlertOpen
import nl.tiebe.otarium.utils.otariumicons.email.EmailOpen
import nl.tiebe.otarium.utils.otariumicons.email.Folder
import nl.tiebe.otarium.utils.otariumicons.email.FolderSwap
import nl.tiebe.otarium.utils.otariumicons.email.Inbox
import nl.tiebe.otarium.utils.otariumicons.email.Pencil
import nl.tiebe.otarium.utils.otariumicons.email.Reply
import nl.tiebe.otarium.utils.otariumicons.email.Send
import nl.tiebe.otarium.utils.otariumicons.email.Share
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
    __AllAssets= listOf(Reply, EmailOpen, EmailAlertOpen, Inbox, DeleteRestore, Send, FolderSwap,
        Attachment, AttachmentOff, Pencil, Delete, Share, Folder, EmailAlert)
    return __AllAssets!!
  }

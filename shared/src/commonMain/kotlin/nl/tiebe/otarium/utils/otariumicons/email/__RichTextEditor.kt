package nl.tiebe.otarium.utils.otariumicons.email

import androidx.compose.ui.graphics.vector.ImageVector
import nl.tiebe.otarium.utils.otariumicons.EmailGroup
import nl.tiebe.otarium.utils.otariumicons.email.richtexteditor.FormatAlignCenter
import nl.tiebe.otarium.utils.otariumicons.email.richtexteditor.FormatAlignLeft
import nl.tiebe.otarium.utils.otariumicons.email.richtexteditor.FormatAlignRight
import nl.tiebe.otarium.utils.otariumicons.email.richtexteditor.FormatBold
import nl.tiebe.otarium.utils.otariumicons.email.richtexteditor.FormatItalic
import nl.tiebe.otarium.utils.otariumicons.email.richtexteditor.FormatListBulleted
import nl.tiebe.otarium.utils.otariumicons.email.richtexteditor.FormatListNumbered
import nl.tiebe.otarium.utils.otariumicons.email.richtexteditor.FormatSize
import nl.tiebe.otarium.utils.otariumicons.email.richtexteditor.FormatStrikethrough
import nl.tiebe.otarium.utils.otariumicons.email.richtexteditor.FormatUnderline
import kotlin.collections.List as ____KtList

public object RichTextEditorGroup

public val EmailGroup.RichTextEditor: RichTextEditorGroup
  get() = RichTextEditorGroup

private var __AllAssets: ____KtList<ImageVector>? = null

public val RichTextEditorGroup.AllAssets: ____KtList<ImageVector>
  get() {
    if (__AllAssets != null) {
      return __AllAssets!!
    }
    __AllAssets= listOf(FormatAlignCenter, FormatAlignLeft, FormatAlignRight, FormatBold,
        FormatItalic, FormatListBulleted, FormatListNumbered, FormatSize, FormatStrikethrough,
        FormatUnderline)
    return __AllAssets!!
  }

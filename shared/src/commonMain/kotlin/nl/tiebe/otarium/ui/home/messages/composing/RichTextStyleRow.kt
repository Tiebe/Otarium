package nl.tiebe.otarium.ui.home.messages.composing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.RichTextState
import nl.tiebe.otarium.utils.OtariumIcons
import nl.tiebe.otarium.utils.otariumicons.Email
import nl.tiebe.otarium.utils.otariumicons.email.RichTextEditor
import nl.tiebe.otarium.utils.otariumicons.email.richtexteditor.*

@OptIn(ExperimentalRichTextApi::class)
@Composable
fun RichTextStyleRow(
    modifier: Modifier = Modifier,
    state: RichTextState,
) {
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        item {
            RichTextStyleButton(
                onClick = {
                    state.addParagraphStyle(
                        ParagraphStyle(
                            textAlign = TextAlign.Left,
                        )
                    )
                },
                isSelected = state.currentParagraphStyle.textAlign == TextAlign.Left,
                icon = OtariumIcons.Email.RichTextEditor.FormatAlignLeft
            )
        }

        item {
            RichTextStyleButton(
                onClick = {
                    state.addParagraphStyle(
                        ParagraphStyle(
                            textAlign = TextAlign.Center
                        )
                    )
                },
                isSelected = state.currentParagraphStyle.textAlign == TextAlign.Center,
                icon = OtariumIcons.Email.RichTextEditor.FormatAlignCenter
            )
        }

        item {
            RichTextStyleButton(
                onClick = {
                    state.addParagraphStyle(
                        ParagraphStyle(
                            textAlign = TextAlign.Right
                        )
                    )
                },
                isSelected = state.currentParagraphStyle.textAlign == TextAlign.Right,
                icon = OtariumIcons.Email.RichTextEditor.FormatAlignRight
            )
        }

        item {
            RichTextStyleButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                isSelected = state.currentSpanStyle.fontWeight == FontWeight.Bold,
                icon = OtariumIcons.Email.RichTextEditor.FormatBold
            )
        }

        item {
            RichTextStyleButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            fontStyle = FontStyle.Italic
                        )
                    )
                },
                isSelected = state.currentSpanStyle.fontStyle == FontStyle.Italic,
                icon = OtariumIcons.Email.RichTextEditor.FormatItalic
            )
        }

        item {
            RichTextStyleButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            textDecoration = TextDecoration.Underline
                        )
                    )
                },
                isSelected = state.currentSpanStyle.textDecoration?.contains(TextDecoration.Underline) == true,
                icon = OtariumIcons.Email.RichTextEditor.FormatUnderline
            )
        }

        item {
            RichTextStyleButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            textDecoration = TextDecoration.LineThrough
                        )
                    )
                },
                isSelected = state.currentSpanStyle.textDecoration?.contains(TextDecoration.LineThrough) == true,
                icon = OtariumIcons.Email.RichTextEditor.FormatStrikethrough
            )
        }

        item {
            RichTextStyleButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            fontSize = 28.sp
                        )
                    )
                },
                isSelected = state.currentSpanStyle.fontSize == 28.sp,
                icon = OtariumIcons.Email.RichTextEditor.FormatSize
            )
        }

        item {
            Box(
                Modifier
                    .height(24.dp)
                    .width(1.dp)
                    .background(Color(0xFF393B3D))
            )
        }

        item {
            RichTextStyleButton(
                onClick = {
                    state.toggleUnorderedList()
                },
                isSelected = state.isUnorderedList,
                icon = OtariumIcons.Email.RichTextEditor.FormatListBulleted,
            )
        }

        item {
            RichTextStyleButton(
                onClick = {
                    state.toggleOrderedList()
                },
                isSelected = state.isOrderedList,
                icon = OtariumIcons.Email.RichTextEditor.FormatListNumbered,
            )
        }

        item {
            Box(
                Modifier
                    .height(24.dp)
                    .width(1.dp)
                    .background(Color(0xFF393B3D))
            )
        }
    }
}
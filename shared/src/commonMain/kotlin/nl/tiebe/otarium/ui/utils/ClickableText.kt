package nl.tiebe.otarium.ui.utils

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import nl.tiebe.otarium.utils.openUrl

@Composable
internal fun ClickableText(text: AnnotatedString, modifier: Modifier = Modifier, style: TextStyle = TextStyle.Default) {
    val onClick: (Int) -> Unit = { offset ->
        text.getStringAnnotations(tag = "URL", start = offset, end = offset).firstOrNull()?.let { annotation ->
            openUrl(annotation.item)
        }
    }

    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    val pressIndicator = Modifier.pointerInput(onClick) {
        detectTapGestures { pos ->
            layoutResult.value?.let { layoutResult ->
                onClick(layoutResult.getOffsetForPosition(pos))
            }
        }
    }

    Text(
        text = text,
        modifier = modifier.then(pressIndicator),
        onTextLayout = { layoutResult.value = it },
        style = style
    )
}
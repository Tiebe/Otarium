package nl.tiebe.otarium.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

@Composable
expect fun HtmlView(
    html: String,
    textColor: Int = 0,
    linkColor: Int = Color(83, 155, 245).toArgb(),
    backgroundColor: Int,
    maxLines: Int = 0
)
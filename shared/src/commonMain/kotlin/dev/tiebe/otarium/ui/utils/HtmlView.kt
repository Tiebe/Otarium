package dev.tiebe.otarium.ui.utils

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.toArgb

@Composable
expect fun HtmlView(
    html: String,
    textColor: Int = LocalTextStyle.current.color.takeOrElse { LocalContentColor.current }.toArgb(),
    linkColor: Int = Color(83, 155, 245).toArgb(),
    maxLines: Int = 0
)
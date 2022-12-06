package nl.tiebe.otarium.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import dev.icerock.moko.resources.AssetResource
import dev.icerock.moko.resources.StringResource
import nl.tiebe.magisterapi.utils.format

expect fun getLocalizedString(string: StringResource): String
@Composable
expect fun getIcon(icon: AssetResource): Painter

fun Float.format(digits: Int) = "%.${digits}f".format(this)
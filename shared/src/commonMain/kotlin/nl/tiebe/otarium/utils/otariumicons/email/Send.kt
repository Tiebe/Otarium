package nl.tiebe.otarium.utils.otariumicons.email

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import nl.tiebe.otarium.utils.otariumicons.EmailGroup

public val EmailGroup.Send: ImageVector
    get() {
        if (_send != null) {
            return _send!!
        }
        _send = Builder(name = "Send", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
                viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(3.0f, 20.0f)
                verticalLineTo(4.0f)
                lineTo(22.0f, 12.0f)
                moveTo(5.0f, 17.0f)
                lineTo(16.85f, 12.0f)
                lineTo(5.0f, 7.0f)
                verticalLineTo(10.5f)
                lineTo(11.0f, 12.0f)
                lineTo(5.0f, 13.5f)
                moveTo(5.0f, 17.0f)
                verticalLineTo(7.0f)
                verticalLineTo(13.5f)
                close()
            }
        }
        .build()
        return _send!!
    }

private var _send: ImageVector? = null

package nl.tiebe.otarium.utils.icons.email

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import nl.tiebe.otarium.utils.icons.EmailGroup

public val EmailGroup.EmailAlertOpen: ImageVector
    get() {
        if (_emailAlertOpen != null) {
            return _emailAlertOpen!!
        }
        _emailAlertOpen = Builder(name = "EmailAlertOpen", defaultWidth = 24.0.dp, defaultHeight =
                24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(2.0f, 8.0f)
                lineTo(10.0f, 13.0f)
                lineTo(18.0f, 8.0f)
                lineTo(10.0f, 3.0f)
                lineTo(2.0f, 8.0f)
                moveTo(20.0f, 8.0f)
                lineTo(20.0f, 18.0f)
                curveTo(20.0f, 19.105f, 19.105f, 20.0f, 18.0f, 20.0f)
                lineTo(2.0f, 20.0f)
                curveTo(0.895f, 20.0f, 0.0f, 19.105f, 0.0f, 18.0f)
                lineTo(0.0f, 8.0f)
                curveTo(0.0f, 7.27f, 0.39f, 6.64f, 0.97f, 6.29f)
                lineTo(10.0f, 0.64f)
                lineTo(19.03f, 6.29f)
                curveTo(19.61f, 6.64f, 20.0f, 7.27f, 20.0f, 8.0f)
                close()
                moveTo(24.0f, 7.0f)
                lineTo(22.0f, 7.0f)
                lineTo(22.0f, 13.0f)
                lineTo(24.0f, 13.0f)
                lineTo(24.0f, 7.0f)
                moveTo(24.0f, 15.0f)
                lineTo(22.0f, 15.0f)
                lineTo(22.0f, 17.0f)
                lineTo(24.0f, 17.0f)
                lineTo(24.0f, 15.0f)
                close()
            }
        }
        .build()
        return _emailAlertOpen!!
    }

private var _emailAlertOpen: ImageVector? = null

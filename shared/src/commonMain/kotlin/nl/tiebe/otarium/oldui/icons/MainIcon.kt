package nl.tiebe.otarium.oldui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.EvenOdd
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val MainIcon: ImageVector
    get() {
        if (_icLauncherForeground != null) {
            return _icLauncherForeground!!
        }
        _icLauncherForeground = Builder(name = "IcLauncherForeground", defaultWidth = 108.0.dp,
                defaultHeight = 108.0.dp, viewportWidth = 108.0f, viewportHeight = 108.0f).apply {
            group {
                path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = EvenOdd) {
                    moveTo(12.35f, 121.46f)
                    curveToRelative(-8.01f, -9.72f, -11.92f, -19.29f, -12.31f, -28.71f)
                    curveTo(-0.78f, 73.01f, 10.92f, 58.28f, 28.3f, 47.67f)
                    curveToRelative(18.28f, -11.16f, 37.08f, -13.93f, 55.36f, -22.25f)
                    curveTo(92.79f, 21.27f, 103.68f, 14.47f, 121.8f, 0.0f)
                    curveToRelative(5.92f, 15.69f, -12.92f, 40.9f, -43.52f, 54.23f)
                    curveToRelative(9.48f, 0.37f, 19.69f, -2.54f, 30.85f, -9.74f)
                    curveToRelative(-0.76f, 19.94f, -16.46f, 32.21f, -51.3f, 36.95f)
                    curveToRelative(7.33f, 2.45f, 16.09f, 2.58f, 27.27f, -0.58f)
                    curveTo(74.33f, 116.81f, 29.9f, 91.06f, 12.35f, 121.46f)
                    lineTo(12.35f, 121.46f)
                    close()
                }
            }
        }
        .build()
        return _icLauncherForeground!!
    }

private var _icLauncherForeground: ImageVector? = null

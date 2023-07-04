package nl.tiebe.otarium.utils.otariumicons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import nl.tiebe.otarium.utils.OtariumIcons

public val OtariumIcons.ContentSave: ImageVector
    get() {
        if (_contentSave != null) {
            return _contentSave!!
        }
        _contentSave = Builder(name = "ContentSave", defaultWidth = 24.0.dp, defaultHeight =
                24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(15.0f, 9.0f)
                horizontalLineTo(5.0f)
                verticalLineTo(5.0f)
                horizontalLineTo(15.0f)
                moveTo(12.0f, 19.0f)
                arcTo(3.0f, 3.0f, 0.0f, false, true, 9.0f, 16.0f)
                arcTo(3.0f, 3.0f, 0.0f, false, true, 12.0f, 13.0f)
                arcTo(3.0f, 3.0f, 0.0f, false, true, 15.0f, 16.0f)
                arcTo(3.0f, 3.0f, 0.0f, false, true, 12.0f, 19.0f)
                moveTo(17.0f, 3.0f)
                horizontalLineTo(5.0f)
                curveTo(3.89f, 3.0f, 3.0f, 3.9f, 3.0f, 5.0f)
                verticalLineTo(19.0f)
                arcTo(2.0f, 2.0f, 0.0f, false, false, 5.0f, 21.0f)
                horizontalLineTo(19.0f)
                arcTo(2.0f, 2.0f, 0.0f, false, false, 21.0f, 19.0f)
                verticalLineTo(7.0f)
                lineTo(17.0f, 3.0f)
                close()
            }
        }
        .build()
        return _contentSave!!
    }

private var _contentSave: ImageVector? = null

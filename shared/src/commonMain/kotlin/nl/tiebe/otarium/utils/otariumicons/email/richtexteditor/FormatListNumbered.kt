package nl.tiebe.otarium.utils.otariumicons.email.richtexteditor

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import nl.tiebe.otarium.utils.otariumicons.email.RichTextEditorGroup

public val RichTextEditorGroup.FormatListNumbered: ImageVector
    get() {
        if (_formatListNumbered != null) {
            return _formatListNumbered!!
        }
        _formatListNumbered = Builder(name = "FormatListNumbered", defaultWidth = 24.0.dp,
                defaultHeight = 24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(7.0f, 13.0f)
                verticalLineTo(11.0f)
                horizontalLineTo(21.0f)
                verticalLineTo(13.0f)
                horizontalLineTo(7.0f)
                moveTo(7.0f, 19.0f)
                verticalLineTo(17.0f)
                horizontalLineTo(21.0f)
                verticalLineTo(19.0f)
                horizontalLineTo(7.0f)
                moveTo(7.0f, 7.0f)
                verticalLineTo(5.0f)
                horizontalLineTo(21.0f)
                verticalLineTo(7.0f)
                horizontalLineTo(7.0f)
                moveTo(3.0f, 8.0f)
                verticalLineTo(5.0f)
                horizontalLineTo(2.0f)
                verticalLineTo(4.0f)
                horizontalLineTo(4.0f)
                verticalLineTo(8.0f)
                horizontalLineTo(3.0f)
                moveTo(2.0f, 17.0f)
                verticalLineTo(16.0f)
                horizontalLineTo(5.0f)
                verticalLineTo(20.0f)
                horizontalLineTo(2.0f)
                verticalLineTo(19.0f)
                horizontalLineTo(4.0f)
                verticalLineTo(18.5f)
                horizontalLineTo(3.0f)
                verticalLineTo(17.5f)
                horizontalLineTo(4.0f)
                verticalLineTo(17.0f)
                horizontalLineTo(2.0f)
                moveTo(4.25f, 10.0f)
                arcTo(0.75f, 0.75f, 0.0f, false, true, 5.0f, 10.75f)
                curveTo(5.0f, 10.95f, 4.92f, 11.14f, 4.79f, 11.27f)
                lineTo(3.12f, 13.0f)
                horizontalLineTo(5.0f)
                verticalLineTo(14.0f)
                horizontalLineTo(2.0f)
                verticalLineTo(13.08f)
                lineTo(4.0f, 11.0f)
                horizontalLineTo(2.0f)
                verticalLineTo(10.0f)
                horizontalLineTo(4.25f)
                close()
            }
        }
        .build()
        return _formatListNumbered!!
    }

private var _formatListNumbered: ImageVector? = null
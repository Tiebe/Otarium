@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package nl.tiebe.otarium.ui.utils.chips

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color

// Copied from material3 v1.2
private const val AnimationDuration = 150

fun TextFieldColors.toChipTextFieldColors(): ChipTextFieldColors {
    return object : ChipTextFieldColors {
        @Composable
        override fun textColor(
            enabled: Boolean,
            isError: Boolean,
            interactionSource: InteractionSource
        ): State<Color> {
            val targetValue = MaterialTheme.colorScheme.onSurface
            return rememberUpdatedState(targetValue)
        }

        @Composable
        override fun cursorColor(isError: Boolean): State<Color> {
            return rememberUpdatedState(if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
        }

        @Composable
        override fun backgroundColor(
            enabled: Boolean,
            isError: Boolean,
            interactionSource: InteractionSource
        ): State<Color> {
            val focused by interactionSource.collectIsFocusedAsState()
            val targetValue = Color.Transparent
            return animateColorAsState(targetValue, tween(durationMillis = AnimationDuration))
        }
    }
}

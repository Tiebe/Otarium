package nl.tiebe.otarium.ui.onboarding.sections

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BottomSection(size: Int, index: Int, onButtonClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            repeat(size) {
                val width = animateDpAsState(
                    targetValue = if (it == index) 25.dp else 10.dp,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                )

                Box(
                    modifier = Modifier
                        .height(10.dp)
                        .width(width.value)
                        .clip(CircleShape)
                        .background(
                            color = if (it == index) MaterialTheme.colorScheme.primary else Color(0XFFF8E2E7)
                        )
                ) {

                }
            }
        }

        FloatingActionButton(
            onClick = onButtonClick,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clip(RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
        ) {
            if (index + 1 < size) {
                Icon(imageVector = Icons.Outlined.KeyboardArrowRight, contentDescription = "Next")
            } else {
                Icon(imageVector = Icons.Filled.Check, contentDescription = "Sign in")
            }
        }
    }
}
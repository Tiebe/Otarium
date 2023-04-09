package nl.tiebe.otarium.ui.home.grades.recentgrades

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.tiebe.magisterapi.response.general.year.grades.RecentGrade
import nl.tiebe.otarium.oldui.utils.topBottomRectBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecentGradeItem(component: RecentGradesChildComponent, grade: RecentGrade) {
    var showExtraInfo by remember { mutableStateOf(false) }


    ListItem(
        modifier = Modifier
            .topBottomRectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline))
            .clickable { showExtraInfo = !showExtraInfo },
        headlineText = { Text(grade.description) },
        supportingText = { Text(grade.subject.description.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
        }) },
        trailingContent = {
            Box(
                modifier = Modifier
                    .size(48.dp)
            ) {
                Text(
                    text = grade.grade,
                    modifier = Modifier
                        .align(Alignment.Center),
                    style = MaterialTheme.typography.displaySmall.copy(fontSize = 18.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${grade.weight}x",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                )
            }
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface
        ),
    )

    AnimatedVisibility(visible = showExtraInfo, enter = expandVertically(), exit = shrinkVertically()) {
        ListItem({Text("fasd")})
    }
}
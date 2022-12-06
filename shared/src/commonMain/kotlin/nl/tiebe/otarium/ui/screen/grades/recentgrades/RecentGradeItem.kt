package nl.tiebe.otarium.ui.screen.grades.recentgrades

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nl.tiebe.magisterapi.response.general.year.grades.RecentGrade
import nl.tiebe.otarium.ui.utils.topBottomRectBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentGradeItem(grade: RecentGrade) {
    ListItem(
        modifier = Modifier
            .topBottomRectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline)),
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
}
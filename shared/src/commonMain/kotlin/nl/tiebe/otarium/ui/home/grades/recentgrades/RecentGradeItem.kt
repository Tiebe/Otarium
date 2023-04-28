package nl.tiebe.otarium.ui.home.grades.recentgrades

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import kotlinx.datetime.toLocalDateTime
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.ui.utils.topBottomRectBorder
import nl.tiebe.otarium.utils.toFormattedString
import nl.tiebe.otarium.utils.ui.format
import nl.tiebe.otarium.utils.ui.getLocalizedString

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
        Column(modifier = Modifier.padding(start = 16.dp)) {
            ListItem(
                modifier = Modifier
                    .topBottomRectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline)),
                headlineText = { Text(grade.grade) },
                overlineText = {
                    Text(getLocalizedString(MR.strings.grade))
                }
            )

            ListItem(
                modifier = Modifier
                    .topBottomRectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline)),
                headlineText = { //parse date
                    val date = grade.enteredOn.substring(0, 26).toLocalDateTime()

                    Text(date.toFormattedString())
                },
                overlineText = {
                    Text(getLocalizedString(MR.strings.entered_on))
                }
            )

            if (grade.weight != 0.0) {
                val beforeAfterAverage = component.calculateAverageBeforeAfter(grade)

                ListItem(
                    modifier = Modifier
                        .topBottomRectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline)),
                    headlineText = {
                        Text("${beforeAfterAverage.first.format(Data.decimals)} -> ${beforeAfterAverage.second.format(Data.decimals)}")
                    },
                    overlineText = {
                        Text(getLocalizedString(MR.strings.average))
                    }
                )
            }
        }
    }
}
package nl.tiebe.otarium.ui.home.grades

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.tiebe.magisterapi.response.general.year.grades.RecentGrade
import kotlinx.datetime.toLocalDateTime
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.logic.root.home.children.grades.GradesComponent
import nl.tiebe.otarium.ui.utils.rectBorder
import nl.tiebe.otarium.utils.toFormattedString
import nl.tiebe.otarium.utils.ui.format
import nl.tiebe.otarium.utils.ui.getLocalizedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GradeItem(component: GradesComponent, grade: RecentGrade) {
    var showExtraInfo by remember { mutableStateOf(false) }

    ListItem(
        modifier = Modifier
            .rectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline))
            .clickable { showExtraInfo = !showExtraInfo },
        headlineContent = { Text(grade.description) },
        overlineContent = { Text(grade.subject.description.replaceFirstChar {
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
                        .padding(bottom = 5.dp)
                        .align(Alignment.Center),
                    style = MaterialTheme.typography.displaySmall.copy(fontSize = 18.sp),
                    maxLines = 1,
                    color = if (Data.markGrades && (grade.grade.replace(",", ".").toFloatOrNull() ?: 10.0f) < Data.passingGrade) MaterialTheme.colorScheme.error else Color.Unspecified,
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
                    .rectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline)),
                headlineContent = { Text(grade.grade) },
                overlineContent = {
                    Text(getLocalizedString(MR.strings.grade))
                }
            )

            ListItem(
                modifier = Modifier
                    .rectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline)),
                headlineContent = { //parse date
                    val date = grade.enteredOn.substring(0, 26).toLocalDateTime()

                    Text(date.toFormattedString())
                },
                overlineContent = {
                    Text(getLocalizedString(MR.strings.entered_on))
                }
            )

            if (grade.weight != 0.0) {
                val beforeAfterAverage = component.calculateAverageBeforeAfter(grade)

                ListItem(
                    modifier = Modifier
                        .rectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline)),
                    headlineContent = {
                        Text("${beforeAfterAverage.first.format(Data.decimals)} -> ${beforeAfterAverage.second.format(Data.decimals)}")
                    },
                    overlineContent = {
                        Text(getLocalizedString(MR.strings.average))
                    }
                )
            }
        }
    }
}
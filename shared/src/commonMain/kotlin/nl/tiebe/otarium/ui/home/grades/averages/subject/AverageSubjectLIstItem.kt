package nl.tiebe.otarium.ui.home.grades.averages.subject

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.toLocalDateTime
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.logic.root.home.children.averages.AveragesComponent
import nl.tiebe.otarium.magister.GradeWithGradeInfo
import nl.tiebe.otarium.magister.ManualGrade
import nl.tiebe.otarium.ui.utils.rectBorder
import nl.tiebe.otarium.utils.toFormattedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GradeListItem(grade: GradeWithGradeInfo) {
    ListItem(
        modifier = Modifier
            .rectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline)),
        headlineContent = { Text(grade.gradeInfo.columnDescription ?: "") },
        supportingContent = { Text(grade.grade.dateEntered?.substring(0, 26)?.toLocalDateTime()?.toFormattedString() ?: "") },
        trailingContent = {
            Box(
                modifier = Modifier
                    .size(48.dp)
            ) {
                Text(
                    text = grade.grade.grade ?: "",
                    modifier = Modifier
                        .align(Alignment.Center),
                    style = MaterialTheme.typography.displaySmall.copy(fontSize = 18.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = if (Data.markGrades && (grade.grade.grade?.replace(",", ".")?.toFloatOrNull() ?: 10.0f) < Data.passingGrade) MaterialTheme.colorScheme.error else Color.Unspecified,
                )

                Text(
                    text = "${grade.gradeInfo.weight}x",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                )
            }
        }
    )
}

@Composable
internal fun ManualGradeListItem(grade: ManualGrade, component: AveragesComponent) {
    ListItem(
        modifier = Modifier
            .rectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline)),
        headlineContent = { Text(grade.name) },
        trailingContent = {
            Row {

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
                        overflow = TextOverflow.Ellipsis,
                        color = if (Data.markGrades && (grade.grade.replace(",", ".").toFloatOrNull() ?: 10.0f) < Data.passingGrade) MaterialTheme.colorScheme.error else Color.Unspecified,
                    )

                    Text(
                        text = "${grade.weight}x",
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                IconButton(onClick = {
                    component.removeManualGrade(grade)
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete grade",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    )
}
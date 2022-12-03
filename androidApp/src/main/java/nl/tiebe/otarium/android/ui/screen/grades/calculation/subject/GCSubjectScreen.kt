package nl.tiebe.otarium.android.ui.screen.grades.calculation.subject

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nl.tiebe.magisterapi.response.general.year.grades.Subject
import nl.tiebe.otarium.android.ui.screen.grades.calculation.GCAverageCalculator
import nl.tiebe.otarium.android.ui.screen.grades.calculation.graph.GCGraph
import nl.tiebe.otarium.android.ui.utils.topBottomRectBorder
import nl.tiebe.otarium.utils.server.ServerGrade
import java.util.*

@Composable
fun GCSubjectScreen(openSubject: MutableState<Subject?>, gradeList: List<ServerGrade>) {
    BackHandler {
        openSubject.value = null
    }

    val subject = openSubject.value!!

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = subject.description.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(20.dp)
        )

        GCGraph(grades = gradeList)

        GCAverageCalculator(grades = gradeList)

        GradeList(grades = gradeList)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradeList(grades: List<ServerGrade>) {
    grades.forEach { grade ->
        ListItem(
            modifier = Modifier
                .topBottomRectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline)),
            headlineText = { Text(grade.gradeInfo.columnDescription ?: ":(") },
            supportingText = { Text(grade.grade.dateEntered ?: ":(") },
            trailingContent = {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                ) {
                    Text(
                        text = grade.grade.grade ?: ":(",
                        modifier = Modifier
                            .align(Alignment.Center),
                        style = MaterialTheme.typography.displaySmall.copy(fontSize = 18.sp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "${grade.gradeInfo.weight}x",
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
}
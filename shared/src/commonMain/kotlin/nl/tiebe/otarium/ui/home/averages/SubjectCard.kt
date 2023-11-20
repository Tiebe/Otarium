package nl.tiebe.otarium.ui.home.averages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.tiebe.magisterapi.response.general.year.grades.Subject
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.logic.root.home.children.averages.AveragesComponent
import nl.tiebe.otarium.magister.GradeWithGradeInfo
import nl.tiebe.otarium.magister.ManualGrade
import nl.tiebe.otarium.ui.home.averages.cards.graph.AverageGraph
import nl.tiebe.otarium.utils.calculateAverage
import nl.tiebe.otarium.utils.ui.format

@Composable
fun SubjectCard(component: AveragesComponent, subject: Subject, realGrades: List<GradeWithGradeInfo>, manualGrades: List<ManualGrade>, showGraph: Boolean) {
    val fullGradeList = derivedStateOf {
        realGrades.map { (it.grade.grade?.replace(',', '.')?.toFloatOrNull() ?: 0f) to it.gradeInfo.weight.toFloat() } +
        manualGrades.map { (it.grade.toFloatOrNull() ?: 0f) to it.weight }
    }

    val average = derivedStateOf { calculateAverage(fullGradeList.value) }

    ElevatedCard(
        modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 5.dp).clickable { component.openSubject(subject) },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = subject.description.capitalize(Locale.current),
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp).fillMaxWidth(0.8f),
                style = MaterialTheme.typography.titleMedium
            )

           ElevatedCard(Modifier.padding(end = 5.dp)) {
                Text(
                    text = average.value.format(1),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(9.dp),
                    color = if (Data.markGrades && average.value < Data.passingGrade) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.displaySmall.copy(fontSize = 18.sp)
                )
            }
        }

        AnimatedVisibility(showGraph) {
            AverageGraph(realGrades, manualGrades, Modifier.padding(5.dp))
        }
    }
}

package nl.tiebe.otarium.ui.home.averages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
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
fun SubjectCard(component: AveragesComponent, subject: Subject, realGrades: List<GradeWithGradeInfo>, manualGrades: List<ManualGrade>) {
    val fullGradeList = derivedStateOf {
        realGrades.map { (it.grade.grade?.replace(',', '.')?.toFloatOrNull() ?: 0f) to it.gradeInfo.weight.toFloat() } +
        manualGrades.map { (it.grade.toFloatOrNull() ?: 0f) to it.weight }
    }

    val average = derivedStateOf { calculateAverage(fullGradeList.value) }

    ElevatedCard(
        modifier = Modifier.padding(15.dp).clickable { component.openSubject(subject) },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = subject.description,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp).fillMaxWidth(0.8f),
                style = MaterialTheme.typography.headlineSmall
            )

            ElevatedCard(Modifier.padding(top = 16.dp, end = 15.dp)) {
                Text(
                    text = average.value.format(1),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(6.dp),
                    color = if (Data.markGrades && average.value < Data.passingGrade) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.displaySmall.copy(fontSize = 18.sp)
                )
            }
        }

        AverageGraph(realGrades, manualGrades)
    }
}

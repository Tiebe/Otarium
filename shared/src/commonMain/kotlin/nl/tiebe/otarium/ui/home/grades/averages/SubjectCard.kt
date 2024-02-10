package nl.tiebe.otarium.ui.home.grades.averages

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import dev.tiebe.magisterapi.response.general.year.grades.Subject
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.logic.root.home.children.averages.AveragesComponent
import nl.tiebe.otarium.magister.GradeWithGradeInfo
import nl.tiebe.otarium.magister.ManualGrade
import nl.tiebe.otarium.ui.home.grades.averages.cards.graph.AverageGraph
import nl.tiebe.otarium.utils.calculateAverage
import nl.tiebe.otarium.utils.ui.format

@Composable
fun SubjectCard(component: AveragesComponent, subject: Subject, realGrades: List<GradeWithGradeInfo>, manualGrades: List<ManualGrade>, showGraph: Boolean) {
    val ptaGrades = derivedStateOf {
        realGrades.filter { it.isPTA }.map { (it.grade.grade?.replace(',', '.')?.toFloatOrNull() ?: 0f) to it.gradeInfo.weight.toFloat() } +
                manualGrades.map { (it.grade.toFloatOrNull() ?: 0f) to it.weight }
    }

    val ptbGrades = derivedStateOf {
        realGrades.filterNot { it.isPTA }.map { (it.grade.grade?.replace(',', '.')?.toFloatOrNull() ?: 0f) to it.gradeInfo.weight.toFloat() } +
        manualGrades.map { (it.grade.toFloatOrNull() ?: 0f) to it.weight }
    }

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
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp).weight(0.75f, fill = false),
                style = MaterialTheme.typography.titleMedium
            )

            Row(Modifier.wrapContentSize()) {
                if (ptaGrades.value.isNotEmpty()) AverageCard(ptaGrades.value, Modifier.padding(end = 5.dp))
                if (ptbGrades.value.isNotEmpty()) AverageCard(ptbGrades.value, Modifier.padding(end = 5.dp))
            }
        }

        AnimatedVisibility(showGraph) {
            AverageGraph(realGrades, manualGrades, Modifier.padding(5.dp))
        }
    }
}


@Composable
fun AverageCard(grades: List<Pair<Float, Float>>, modifier: Modifier = Modifier) {
    ElevatedCard(modifier = Modifier.height(40.dp).width(40.dp + (Data.decimals * 10.dp)).then(modifier)) {
        Box(modifier = Modifier.fillMaxSize().padding(start = 3.dp, end = 3.dp), contentAlignment = Alignment.Center) {
            val average = derivedStateOf { calculateAverage(grades) }

            Text(
                text = average.value.format(Data.decimals),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                maxLines = 1,
                color = if (Data.markGrades && average.value < Data.passingGrade) MaterialTheme.colorScheme.error else Color.Unspecified,
            )
        }
    }
}
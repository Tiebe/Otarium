package nl.tiebe.otarium.oldui.screen.grades.calculation.subject

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import dev.tiebe.magisterapi.response.general.year.grades.Subject
import nl.tiebe.otarium.magister.GradeWithGradeInfo
import nl.tiebe.otarium.oldui.screen.grades.calculation.cards.calculateAverage
import nl.tiebe.otarium.oldui.utils.topBottomRectBorder
import nl.tiebe.otarium.utils.ui.format

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GCSubjectList(componentContext: ComponentContext, grades: List<GradeWithGradeInfo>) {
    val openSubject = remember { mutableStateOf<Subject?>(null)}

    val subjects = grades.map { it.grade.subject }.distinct().sortedBy { it.description.lowercase() }

    if (openSubject.value != null) { GCSubjectScreen(componentContext = componentContext, openSubject = openSubject, gradeList = grades.filter { it.grade.subject.id == openSubject.value?.id }) }
    else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            subjects.forEach { subject ->
                ListItem(
                    modifier = Modifier
                        .topBottomRectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline))
                        .clickable { openSubject.value = subject },
                    headlineText = { Text(subject.description.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase() else it.toString()
                    }) },
                    trailingContent = {
                        Text(
                            text = calculateAverage(grades.filter { it.grade.subject.id == subject.id }).format(1),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(2.dp),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.displaySmall.copy(fontSize = 18.sp)
                        )
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.inverseOnSurface
                    ),
                )
            }
        }
    }
}
package nl.tiebe.otarium.ui.screen.grades.calculation.subject

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
import nl.tiebe.magisterapi.response.general.year.grades.Subject
import nl.tiebe.otarium.ui.utils.topBottomRectBorder
import nl.tiebe.otarium.utils.server.ServerGrade
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GCSubjectList(grades: List<ServerGrade>) {
    val openSubject = remember { mutableStateOf<Subject?>(null)}

    val subjects = grades.map { it.grade.subject }.distinct().sortedBy { it.description.lowercase() }

    if (openSubject.value != null) { GCSubjectScreen(openSubject = openSubject, gradeList = grades.filter { it.grade.subject.id == openSubject.value?.id }) }
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
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.inverseOnSurface
                    ),
                )
            }
        }
    }
}
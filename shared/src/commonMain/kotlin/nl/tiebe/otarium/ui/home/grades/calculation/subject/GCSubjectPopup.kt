package nl.tiebe.otarium.ui.home.grades.calculation.subject

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.tiebe.magisterapi.response.general.year.grades.Subject
import kotlinx.datetime.toLocalDateTime
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.magister.GradeWithGradeInfo
import nl.tiebe.otarium.magister.ManualGrade
import nl.tiebe.otarium.ui.home.grades.calculation.GradeCalculationChildComponent
import nl.tiebe.otarium.ui.home.grades.calculation.calculateAverage
import nl.tiebe.otarium.ui.home.grades.calculation.cards.GCAverageCalculator
import nl.tiebe.otarium.ui.home.grades.calculation.cards.graph.GCGraph
import nl.tiebe.otarium.ui.utils.BackButton
import nl.tiebe.otarium.ui.utils.topBottomRectBorder
import nl.tiebe.otarium.utils.toFormattedString
import nl.tiebe.otarium.utils.ui.format

@Composable
internal fun GCSubjectPopup(component: GradeCalculationChildComponent, subject: Subject, realGradeList: List<GradeWithGradeInfo>) {
    val manualGradeList = component.manualGradesList.subscribeAsState()
    val gradeList = remember {
        realGradeList.map {
            (it.grade.grade?.replace(',', '.')?.toFloatOrNull() ?: 0f) to it.gradeInfo.weight.toFloat()
        } + manualGradeList.value.map {
            (it.grade.toFloatOrNull() ?: 0f) to it.weight
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
            Text(
                text = subject.description.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase() else it.toString()
                },
                maxLines = 1,
                style = MaterialTheme.typography.headlineMedium,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.7f).align(Alignment.Center)
            )

            ElevatedCard(modifier = Modifier.size(50.dp).align(Alignment.CenterEnd)) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = calculateAverage(gradeList).format(Data.decimals),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        GCGraph(grades = realGradeList, manualGrades = manualGradeList.value)

        GCAverageCalculator(grades = realGradeList, manualGrades = manualGradeList.value)

        val addItemPopout = component.addManualGradePopupOpen.subscribeAsState().value

        Row(Modifier.padding(top = 20.dp, start = 20.dp, bottom = 12.dp)) {
            Text(
                text = "Manually added grades",
                style = MaterialTheme.typography.headlineSmall,
            )

            Spacer(modifier = Modifier.weight(1f))

            val rotation by animateFloatAsState(
                targetValue = if (addItemPopout) 45f else 0f
            )

            IconButton(onClick = {
                component.addManualGradePopupOpen.value = !addItemPopout
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add grade manually",
                    modifier = Modifier.size(24.dp).rotate(rotation)
                )
            }
        }


        AnimatedVisibility(visible = addItemPopout, enter = expandVertically(), exit = shrinkVertically()) {
            Column {
                AddGradeManually(component)
            }
        }

        manualGradeList.value.reversed().forEach {
            ManualGradeListItem(it, component)
        }

        Text(
            text = "Grades",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(20.dp)
        )

        realGradeList.reversed().forEach { grade ->
            GradeListItem(grade)
        }
    }

    Box(Modifier.fillMaxSize().padding(top = 20.dp, start = 20.dp, bottom = 12.dp)) {
        BackButton(
            Modifier.align(Alignment.TopStart),
            {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                )
            }) {
            component.closeSubject()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddGradeManually(component: GradeCalculationChildComponent) {
    val name = remember { mutableStateOf("") }
    val nameError = remember { mutableStateOf(false) }

    val grade = remember { mutableStateOf("") }
    val gradeError = remember { mutableStateOf(false) }

    val weight = remember { mutableStateOf("") }
    val weightError = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Name") },
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(15.dp))

        OutlinedTextField(
            value = grade.value,
            onValueChange = { value ->
                if ((value.replace(",", ".").replace(".", "").all { it.isDigit() } &&
                            value.replace(",", ".").toFloatOrNull() != null) || value.isBlank()) grade.value = value },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = { Text("Grade") },
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(5.dp))

        OutlinedTextField(
            value = weight.value,
            onValueChange = { value ->
                if ((value.replace(",", ".").replace(".", "").all { it.isDigit() } &&
                            value.replace(",", ".").toFloatOrNull() != null) || value.isBlank()) weight.value = value },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = { Text("Weight") },
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(20.dp))

        Button(onClick = {
            nameError.value = name.value.isBlank()
            gradeError.value = grade.value.isBlank()
            weightError.value = weight.value.isBlank()

            if (name.value.isNotBlank() && grade.value.isNotBlank() && weight.value.isNotBlank()) {
                val manualGrade = ManualGrade(
                    name = name.value,
                    grade = grade.value,
                    weight = weight.value.toFloatOrNull() ?: 0f
                )

                component.addManualGrade(manualGrade)

                name.value = ""
                grade.value = ""
                weight.value = ""

                component.addManualGradePopupOpen.value = false
            }

        }) {
            Text("Add")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GradeListItem(grade: GradeWithGradeInfo) {
    ListItem(
        modifier = Modifier
            .topBottomRectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline)),
        headlineText = { Text(grade.gradeInfo.columnDescription ?: "") },
        supportingText = { Text(grade.grade.dateEntered?.substring(0, 26)?.toLocalDateTime()?.toFormattedString() ?: "") },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ManualGradeListItem(grade: ManualGrade, component: GradeCalculationChildComponent) {
    ListItem(
        modifier = Modifier
            .topBottomRectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline)),
        headlineText = { Text(grade.name) },
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
                        overflow = TextOverflow.Ellipsis
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
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface
        ),
    )
}
package dev.tiebe.otarium.ui.home.grades.calculation.subject

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.tiebe.magisterapi.response.general.year.grades.Subject
import dev.tiebe.otarium.MR
import dev.tiebe.otarium.magister.ManualGrade
import dev.tiebe.otarium.ui.home.grades.calculation.GradeCalculationChildComponent
import dev.tiebe.otarium.utils.ui.getLocalizedString


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddGradeManually(component: GradeCalculationChildComponent, subject: Subject) {
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
            label = { Text(getLocalizedString(MR.strings.manual_name)) },
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
            label = { Text(getLocalizedString(MR.strings.manual_grade)) },
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
            label = { Text(getLocalizedString(MR.strings.manual_weight)) },
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
                    weight = weight.value.toFloatOrNull() ?: 0f,
                    subjectId = subject.id
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


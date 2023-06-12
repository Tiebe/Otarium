package dev.tiebe.otarium.ui.home.grades.calculation.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.tiebe.otarium.Data
import dev.tiebe.otarium.MR
import dev.tiebe.otarium.magister.GradeWithGradeInfo
import dev.tiebe.otarium.magister.ManualGrade
import dev.tiebe.otarium.ui.home.grades.calculation.calculateAverage
import dev.tiebe.otarium.ui.home.grades.calculation.calculateNew
import dev.tiebe.otarium.utils.ui.format
import dev.tiebe.otarium.utils.ui.getLocalizedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GCAverageCalculator(grades: List<GradeWithGradeInfo>, manualGrades: List<ManualGrade>) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Text(
            text = getLocalizedString(MR.strings.calculation),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        var type by remember { mutableStateOf(1) }

        Row(modifier = Modifier.padding(end = 20.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Row(Modifier
                .selectable(
                    selected = (type == 1),
                    onClick = {
                        type = 1
                    }),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = type == 1, onClick = { type = 1 })
                Text(getLocalizedString(MR.strings.calculate_new_average))
            }

            Row(Modifier
                .selectable(
                    selected = (type == 2),
                    onClick = {
                        type = 2
                    }),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = type == 2, onClick = { type = 2 })
                Text(getLocalizedString(MR.strings.calculate_new_grade))

            }
        }
        Row(Modifier.padding(5.dp)) {
            var calculatedAverage by remember { mutableStateOf<Float?>(null)}

            var enteredGrade by remember { mutableStateOf("") }
            var enteredWeight by remember { mutableStateOf("") }

            Column(Modifier.padding(start = 5.dp)) {
                TextField(
                    value = enteredGrade,
                    onValueChange = { value ->
                        if ((value.replace(",", ".").replace(".", "").all { it.isDigit() } &&
                        value.replace(",", ".").toFloatOrNull() != null) || value.isBlank()) enteredGrade = value },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    placeholder = { Text(
                        getLocalizedString(
                            if (type == 1) MR.strings.new_grade_calculation_placeholder else MR.strings.average_calculation_placeholder)
                    ) },
                    modifier = Modifier.width(200.dp)
                )

                Spacer(modifier = Modifier.padding(top = 6.dp))

                TextField(
                    value = enteredWeight,
                    onValueChange = { value ->
                        if ((value.replace(",", ".").replace(".", "").all { it.isDigit() } &&
                                value.replace(",", ".").toFloatOrNull() != null) || value.isBlank()) enteredWeight = value },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    placeholder = { Text(getLocalizedString(MR.strings.weight_calculation_placeholder)) },
                    modifier = Modifier.width(200.dp)
                )
            }

            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text(
                    text = if (calculatedAverage != null) calculatedAverage?.format(Data.decimals) ?: "" else "",
                    style = MaterialTheme.typography.displayMedium,
                    color = if (Data.markGrades && (calculatedAverage ?: 10.0f) < Data.passingGrade) MaterialTheme.colorScheme.error else Color.Unspecified,
                )

                Button(modifier = Modifier.padding(top = 5.dp), onClick = {
                    calculatedAverage = if (type == 1)
                        calculateAverage(grades.map {
                            (it.grade.grade?.replace(',', '.')?.toFloatOrNull() ?: 0f) to it.gradeInfo.weight.toFloat()
                        } + manualGrades.map {
                            (it.grade.toFloatOrNull() ?: 0f) to it.weight
                        },
                            enteredGrade.replace(",", ".").toFloatOrNull() ?: 0f,
                            enteredWeight.replace(",", ".").toFloatOrNull() ?: 0f)
                    else {
                        calculateNew(grades.map {
                            (it.grade.grade?.replace(',', '.')?.toFloatOrNull() ?: 0f) to it.gradeInfo.weight.toFloat()
                        } + manualGrades.map {
                            (it.grade.toFloatOrNull() ?: 0f) to it.weight
                        },
                            enteredGrade.replace(",", ".").toFloatOrNull() ?: 0f,
                            enteredWeight.replace(",", ".").toFloatOrNull() ?: 0f)
                    }
                }) {
                    Text(getLocalizedString(MR.strings.calculate))
                }
            }
        }
    }
}

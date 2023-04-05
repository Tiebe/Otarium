package nl.tiebe.otarium.ui.home.grades

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.ui.home.grades.calculation.GradeCalculationChildComponent
import nl.tiebe.otarium.ui.home.grades.calculation.screen.GradeCalculationChild
import nl.tiebe.otarium.ui.home.grades.recentgrades.RecentGradesChild
import nl.tiebe.otarium.ui.home.grades.recentgrades.RecentGradesChildComponent
import nl.tiebe.otarium.utils.ui.getLocalizedString

@Composable
internal fun GradesScreen(component: GradesComponent) {
    val dialog = component.dialog.subscribeAsState().value
    val overlay = dialog.child ?: return

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = overlay.configuration.id) {
            Tab(
                selected = overlay.configuration is GradesComponent.GradesChild.RecentGrades,
                onClick = { component.changeChild(GradesComponent.GradesChild.RecentGrades) },
                modifier = Modifier.height(53.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = getLocalizedString(MR.strings.gradesItem),
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp
                    )
                }
            }

            Tab(
                selected = overlay.configuration is GradesComponent.GradesChild.Calculation,
                onClick = { component.changeChild(GradesComponent.GradesChild.Calculation) },
                modifier = Modifier.height(53.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = getLocalizedString(MR.strings.grade_calculation_item),
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp
                    )
                }
            }
        }

        when (val childComponent = overlay.instance) {
            is RecentGradesChildComponent -> RecentGradesChild(childComponent)
            is GradeCalculationChildComponent -> GradeCalculationChild(childComponent)
        }
    }
}
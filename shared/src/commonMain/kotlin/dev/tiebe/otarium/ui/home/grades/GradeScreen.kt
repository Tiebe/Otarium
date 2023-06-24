package dev.tiebe.otarium.ui.home.grades

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.tiebe.otarium.MR
import dev.tiebe.otarium.logic.root.home.children.grades.GradesComponent
import dev.tiebe.otarium.ui.home.grades.calculation.screen.GradeCalculationChild
import dev.tiebe.otarium.ui.home.grades.recentgrades.RecentGradesChild
import dev.tiebe.otarium.utils.ui.getLocalizedString

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun GradesScreen(component: GradesComponent) {
    var page by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = page
        ) {
            Tab(
                selected = page == 0,
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
                selected = page == 1,
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


        when (page) {
            0 -> RecentGradesChild(component.recentGradeComponent)
            1 -> GradeCalculationChild(component.calculationChildComponent)
        }

        component.currentPage.subscribe {
            page = it
        }
    }
}
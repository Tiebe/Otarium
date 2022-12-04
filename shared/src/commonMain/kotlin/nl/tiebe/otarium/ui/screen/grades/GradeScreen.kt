package nl.tiebe.otarium.ui.screen.grades

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.ui.screen.grades.calculation.screen.GCScreen
import nl.tiebe.otarium.ui.screen.grades.recentgrades.RecentGradeScreen
import nl.tiebe.otarium.utils.getLocalizedString

@Composable
fun GradeScreen() {
    var selectedTab by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            Tab(selectedTab == 0, onClick = { selectedTab = 0 }, modifier = Modifier.height(53.dp)) {
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

            Tab(selectedTab == 1, onClick = { selectedTab = 1 }, modifier = Modifier.height(53.dp)) {
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

        when (selectedTab) {
            0 -> RecentGradeScreen()
            1 -> GCScreen()
        }
    }
}
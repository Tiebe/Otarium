package nl.tiebe.otarium.android.ui.screen.grades

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nl.tiebe.otarium.android.R
import nl.tiebe.otarium.android.ui.screen.grades.calculation.screen.GCScreen
import nl.tiebe.otarium.android.ui.screen.grades.recentgrades.RecentGradeScreen

@Composable
fun GradeScreen() {
    var selectedTab by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            Tab(selectedTab == 0, onClick = { selectedTab = 0 }, modifier = Modifier.height(53.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.gradesItem),
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
                        text = stringResource(id = R.string.grade_calculation_item),
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
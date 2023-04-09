package nl.tiebe.otarium.ui.home.grades.recentgrades

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.tiebe.magisterapi.response.general.year.grades.RecentGrade


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecentGradePopup(component: RecentGradesChildComponent, recentGrade: RecentGrade) {
    Surface(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Text(
                text = recentGrade.description,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier
            )

            Text(
                text = recentGrade.subject.description,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
            )

        }
    }
}
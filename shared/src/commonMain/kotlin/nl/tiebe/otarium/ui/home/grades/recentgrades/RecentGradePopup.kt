package nl.tiebe.otarium.ui.home.grades.recentgrades

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.tiebe.magisterapi.response.general.year.grades.RecentGrade


@Composable
internal fun RecentGradePopup(recentGrade: RecentGrade) {
    Surface(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            //todo: make this screen or smth
        }
    }
}
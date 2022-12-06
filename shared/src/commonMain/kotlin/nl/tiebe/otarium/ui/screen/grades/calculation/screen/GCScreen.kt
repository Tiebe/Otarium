package nl.tiebe.otarium.ui.screen.grades.calculation.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import nl.tiebe.otarium.ui.screen.grades.calculation.subject.GCSubjectList

@Composable
fun GCScreen() {
    val viewModel = GCScreenModel()
    when (val state = viewModel.state.collectAsState().value) {
        GCScreenModel.State.Loading -> {
            Text("Loading")
        }
        is GCScreenModel.State.Data -> {
            GCSubjectList(state.data)
        }
        is GCScreenModel.State.Failed -> {
            Text("Something went wrong while retrieving your grades")
        }
    }
}
package nl.tiebe.otarium.android.ui.screen.grades.calculation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun GCScreen() {
    val viewModel = viewModel<GCScreenModel>()
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
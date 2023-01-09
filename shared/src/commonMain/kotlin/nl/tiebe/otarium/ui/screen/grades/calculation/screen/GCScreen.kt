package nl.tiebe.otarium.ui.screen.grades.calculation.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import nl.tiebe.otarium.ui.screen.grades.calculation.subject.GCSubjectList

@Composable
internal fun GCScreen() {
    val screenModel by remember { mutableStateOf(GCScreenModel()) }
    val state by screenModel.state.subscribeAsState()

    when (state) {
        GCScreenModel.State.Loading -> {
            Text("Loading")
        }
        is GCScreenModel.State.Data -> {
            GCSubjectList((state as GCScreenModel.State.Data).data)
        }
        is GCScreenModel.State.Failed -> {
            Text("Something went wrong while retrieving your grades")
        }
    }
}

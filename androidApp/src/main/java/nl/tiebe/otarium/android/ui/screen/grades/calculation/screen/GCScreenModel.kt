package nl.tiebe.otarium.android.ui.screen.grades.calculation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import nl.tiebe.magisterapi.response.general.year.grades.GradeColumn
import nl.tiebe.otarium.magister.Tokens
import nl.tiebe.otarium.utils.server.ServerGrade
import nl.tiebe.otarium.utils.server.getGradesFromServer

class GCScreenModel : ViewModel() {
    sealed class State {
        object Loading: State()
        data class Data(val data: List<ServerGrade>): State()
        object Failed: State()
    }

    private var _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            while (isActive) {
                try {
                    Tokens.getPastTokens()?.accessTokens?.accessToken?.let { token ->
                        _state.value = State.Data(getGradesFromServer(token)?.filter {
                            it.grade.gradeColumn.type == GradeColumn.Type.Grade &&
                                    it.grade.grade?.replace(",", ".")?.toDoubleOrNull() != null
                        } ?: return@let)
                        return@launch
                    }
                } catch (e: Exception) { e.printStackTrace() }
                _state.value = State.Failed
                break
            }
        }
    }
}
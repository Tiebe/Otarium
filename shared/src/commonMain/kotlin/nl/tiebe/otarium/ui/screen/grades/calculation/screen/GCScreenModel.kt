package nl.tiebe.otarium.ui.screen.grades.calculation.screen

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.tiebe.magisterapi.response.general.year.grades.GradeColumn
import nl.tiebe.otarium.magister.Tokens
import nl.tiebe.otarium.utils.server.ServerGrade
import nl.tiebe.otarium.utils.server.getGradesFromServer

class GCScreenModel {
    sealed class State {
        object Loading: State()
        data class Data(val data: List<ServerGrade>): State()
        object Failed: State()
    }

    private var _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    init {
        runBlocking {
            launch {
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
}
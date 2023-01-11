package nl.tiebe.otarium.ui.screen.grades.calculation.screen

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.*
import nl.tiebe.magisterapi.response.general.year.grades.GradeColumn
import nl.tiebe.otarium.magister.Tokens
import nl.tiebe.otarium.useServer
import nl.tiebe.otarium.utils.server.ServerGrade
import nl.tiebe.otarium.utils.server.getGradesFromServer

@OptIn(DelicateCoroutinesApi::class)

//TODO: FIX THIS
class GCScreenModel {
    sealed class State {
        object Loading: State()
        data class Data(val data: List<ServerGrade>): State()
        object Failed: State()
    }

    private var _state = MutableValue<State>(State.Loading)
    val state: Value<State> = _state

    init {
        runBlocking {
            try {
                if (useServer()) {
                    Tokens.getPastTokens()?.accessTokens?.accessToken?.let { token ->
                        _state.value = State.Data(getGradesFromServer(token)?.filter {
                            it.grade.gradeColumn.type == GradeColumn.Type.Grade &&
                                    it.grade.grade?.replace(",", ".")?.toDoubleOrNull() != null
                        } ?: return@let)
                        return@runBlocking
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        _state.value = State.Failed
    }
}
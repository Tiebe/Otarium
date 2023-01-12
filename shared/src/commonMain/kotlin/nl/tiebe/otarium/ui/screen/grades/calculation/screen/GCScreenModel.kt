package nl.tiebe.otarium.ui.screen.grades.calculation.screen

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnCreate
import kotlinx.coroutines.*
import nl.tiebe.magisterapi.response.general.year.grades.GradeColumn
import nl.tiebe.otarium.magister.Tokens
import nl.tiebe.otarium.useServer
import nl.tiebe.otarium.utils.refreshGrades
import nl.tiebe.otarium.utils.server.ServerGrade
import nl.tiebe.otarium.utils.server.getGradesFromServer

@OptIn(DelicateCoroutinesApi::class)

//TODO: FIX THIS
class GCScreenModel(componentContext: ComponentContext) : ComponentContext by componentContext {
    sealed class State {
        object Loading: State()
        data class Data(val data: List<ServerGrade>): State()
        object Failed: State()
    }

    private var _state = MutableValue<State>(State.Loading)
    val state: Value<State> = _state

    init {
        lifecycle.doOnCreate {
            runBlocking {
                launch {
                    //todo this shouldnt block the thread
                    try {
                        if (useServer()) {
                            Tokens.getPastTokens()?.accessTokens?.accessToken?.let { token ->
                                _state.value = State.Data(getGradesFromServer(token)?.filter {
                                    it.grade.gradeColumn.type == GradeColumn.Type.Grade &&
                                            it.grade.grade?.replace(",", ".")
                                                ?.toDoubleOrNull() != null
                                } ?: return@let)
                                return@launch
                            }
                        } else {
                            _state.value = State.Data(refreshGrades()?.filter {
                                it.grade.gradeColumn.type == GradeColumn.Type.Grade &&
                                        it.grade.grade?.replace(",", ".")?.toDoubleOrNull() != null
                            } ?: return@launch)
                            println(_state.value)
                            return@launch
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    _state.value = State.Failed
                }
            }
        }
    }
}
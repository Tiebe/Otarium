package nl.tiebe.otarium.wear.ui.home.grades

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TitleCard
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.tiebe.magisterapi.response.general.year.grades.RecentGrade
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.logic.root.home.children.grades.RecentGradesComponent
import nl.tiebe.otarium.utils.ui.getLocalizedString

@Composable
fun GradeScreen(component: RecentGradesComponent) {
    component.refreshGrades()
    val grades = component.grades.subscribeAsState().value

    val listState = rememberScalingLazyListState()
    val reachedEnd by remember { derivedStateOf { listState.centerItemIndex >= (grades.size * 0.5).toInt() } }

    LaunchedEffect(reachedEnd) {
        if (!component.refreshState.value)
            component.loadNextGrades()
    }

    ScalingLazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
        item { ListHeader {
            Text(text = getLocalizedString(MR.strings.gradesItem))
        } }
        for (grade in grades) {
            item { GradeItem(grade) }
        }
    }
}

@Composable
fun GradeItem(grade: RecentGrade) {
    TitleCard(onClick = {}, title = {
        Text(text = grade.subject.description + ": " + grade.description)
    }, subtitle = {
        Text(grade.grade, maxLines = 1)
    })
}
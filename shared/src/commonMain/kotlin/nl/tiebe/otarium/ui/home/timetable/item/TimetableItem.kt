package nl.tiebe.otarium.ui.home.timetable.item

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import kotlinx.datetime.*
import nl.tiebe.otarium.magister.getAgendaForDay
import nl.tiebe.otarium.ui.home.timetable.TimetableComponent
import kotlin.math.floor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TimetableItem(
    component: TimetableComponent,
    page: Int
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val timetable = component.timetable.subscribeAsState()
        val timetableForDay = component.getTimetableForWeek(timetable.value, startOfWeekDate).getAgendaForDay(page - (pageWeek * component.days.size))
        val timetableMap = timetableForDay.map { it to component.getTotalAmountOfOverlaps(it, timetableForDay) }


        timetableMap.forEach { pair ->

        }
    }
}
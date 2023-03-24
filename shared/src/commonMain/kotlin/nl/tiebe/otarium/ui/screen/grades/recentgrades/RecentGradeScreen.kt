package nl.tiebe.otarium.ui.screen.grades.recentgrades

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.tiebe.magisterapi.utils.MagisterException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.magister.getRecentGrades

@Composable
internal fun RecentGradeScreen() {
    val refreshState = rememberSwipeRefreshState(false)
    var recentGrades by remember { mutableStateOf(Data.selectedAccount.grades) }
    val scope = rememberCoroutineScope()

    val refreshRecentGrades: (coroutineScope: CoroutineScope) -> Unit = {
        it.launch {
            refreshState.isRefreshing = true
            try {
                recentGrades = Data.selectedAccount.getRecentGrades()
            } catch (e: MagisterException) {
                e.printStackTrace()
            } catch (_: Exception) {
            }
            refreshState.isRefreshing = false
        }
    }

    LaunchedEffect(Unit) {
        refreshRecentGrades(this)
    }

    SwipeRefresh(state = refreshState, onRefresh = { refreshRecentGrades(scope) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            recentGrades.forEach {
                RecentGradeItem(grade = it)
            }
        }
    }
}

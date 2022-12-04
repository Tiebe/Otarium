package nl.tiebe.otarium.ui.screen.grades.recentgrades

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import nl.tiebe.magisterapi.utils.MagisterException
import nl.tiebe.otarium.magister.Tokens
import nl.tiebe.otarium.magister.getRecentGrades
import nl.tiebe.otarium.magister.getSavedGrades
import nl.tiebe.otarium.utils.server.getMagisterTokens

@Composable
fun RecentGradeScreen() {
    val refreshState = rememberSwipeRefreshState(false)
    var recentGrades by remember { mutableStateOf(getSavedGrades()) }
    val scope = rememberCoroutineScope()

    SwipeRefresh(state = refreshState, onRefresh = {
        scope.launch {
            refreshState.isRefreshing = true
            try {
                getMagisterTokens(Tokens.getPastTokens()?.accessTokens?.accessToken)?.let { tokens ->
                    println("refreshing grades")


                    recentGrades = getRecentGrades(tokens.accountId, tokens.tenantUrl, tokens.tokens.accessToken)
                }
            } catch (e: MagisterException) {
                e.printStackTrace()
            } catch (_: Exception) {}
            refreshState.isRefreshing = false
        }
    }) {
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

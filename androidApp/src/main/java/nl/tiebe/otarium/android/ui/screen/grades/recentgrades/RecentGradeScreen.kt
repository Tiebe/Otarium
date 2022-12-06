package nl.tiebe.otarium.android.ui.screen.grades.recentgrades

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
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
import java.util.*

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
                    Log.d("Grades", "Refreshing grades")


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

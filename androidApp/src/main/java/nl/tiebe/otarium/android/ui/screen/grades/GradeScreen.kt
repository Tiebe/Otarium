package nl.tiebe.otarium.android.ui.screen.grades

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import nl.tiebe.magisterapi.utils.MagisterException
import nl.tiebe.otarium.android.ui.utils.topBottomRectBorder
import nl.tiebe.otarium.magister.Tokens
import nl.tiebe.otarium.magister.getRecentGrades
import nl.tiebe.otarium.magister.getSavedGrades
import nl.tiebe.otarium.utils.server.getMagisterTokens
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradeScreen() {
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
                ListItem(
                    modifier = Modifier
                        .topBottomRectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline)),
                    headlineText = { Text(it.description) },
                    supportingText = { Text(it.subject.description.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.getDefault()
                        ) else it.toString()
                    }) },
                    trailingContent = {
                                      Box(
                                          modifier = Modifier
                                              .size(48.dp)
                                      ) {
                                          Text(
                                              text = it.grade,
                                              modifier = Modifier
                                                  .align(Alignment.Center),
                                              style = MaterialTheme.typography.displaySmall.copy(fontSize = 18.sp),
                                              maxLines = 1,
                                              overflow = TextOverflow.Ellipsis
                                          )

                                          Text(
                                              text = "${it.weight}x",
                                              modifier = Modifier
                                                  .align(Alignment.BottomEnd)
                                          )
                                      }
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.inverseOnSurface
                    ),
                )
            }
        }
    }
}

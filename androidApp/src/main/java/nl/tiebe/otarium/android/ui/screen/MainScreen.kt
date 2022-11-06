package nl.tiebe.otarium.android.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import nl.tiebe.magisterapi.response.general.year.agenda.AgendaItem
import nl.tiebe.otarium.android.R
import nl.tiebe.otarium.android.ui.utils.pagerTabIndicatorOffset
import nl.tiebe.otarium.magister.getAgendaForDay
import nl.tiebe.otarium.magister.getSavedAgenda

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Preview(showBackground = true)
@Composable
fun MainScreen() {
    var state by remember { mutableStateOf(0) }
    val pagerState = rememberPagerState()
    val titles = listOf(stringResource(R.string.mon), stringResource(R.string.tue), stringResource(R.string.wed), stringResource(R.string.thu), stringResource(R.string.fri))

    var agenda = getSavedAgenda()
    val selectedWeekAgenda: MutableList<List<AgendaItem>> = mutableListOf()

    titles.indices.forEach { selectedWeekAgenda.add(it, agenda.getAgendaForDay(it)) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = state, indicator = { tabPositions -> // 3.
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = state == index,
                    onClick = { state = index },
                    text = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center)
                            Text(text = selectedWeekAgenda[index][0].start.substring(5, 10).split("-").reversed().joinToString("-"),
                                textAlign = TextAlign.Center)
                        }

                    }
                )
            }
        }
        HorizontalPager(
            count = titles.size,
            state = pagerState,
        ) { tabIndex ->
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                for (item in selectedWeekAgenda[tabIndex]) {
                    ListItem(
                        headlineText = { Text(item.description ?: "") },
                    )
                    Divider()
                }
            }


/*            Button(onClick = {
                runBlocking {
                    launch {
                        val magisterTokens = getMagisterTokens(
                            Tokens.getPastTokens()?.accessTokens?.accessToken ?: return@launch
                        )

                        Log.d(
                            "MAGISTER", Json.encodeToString(
                                getMagisterAgenda(
                                    magisterTokens.accountId,
                                    magisterTokens.tenantUrl,
                                    magisterTokens.tokens.accessToken
                                )
                            )
                        )
                    }
                }
            }) {
                Text("Retrieve")
            }*/
        }
    }
}
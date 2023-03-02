package nl.tiebe.otarium.oldui.screen.agenda

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import dev.tiebe.magisterapi.utils.MagisterException
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.magister.*
import nl.tiebe.otarium.utils.ui.getLocalizedString
import kotlin.math.floor

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun AgendaScreen(componentContext: ComponentContext) {
    val account = remember { Data.selectedAccount }

    val scope = rememberCoroutineScope()

    val now = remember { Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam")) }
    val firstDayOfWeek = remember { now.date.minus(now.date.dayOfWeek.ordinal, DateTimeUnit.DAY) }

    val initialPage = remember { 500 + now.date.dayOfWeek.ordinal }
    val dayPagerState = rememberPagerState(initialPage)

    val days = listOf(
        getLocalizedString(MR.strings.monday),
        getLocalizedString(MR.strings.tuesday),
        getLocalizedString(MR.strings.wednesday),
        getLocalizedString(MR.strings.thursday),
        getLocalizedString(MR.strings.friday),
        getLocalizedString(MR.strings.saturday),
        getLocalizedString(MR.strings.sunday)
    )

    val savedAgenda = remember { account.agenda }
    val loadedAgendas = remember {
        mutableStateMapOf(
            Pair(100, days.indices.map {
                savedAgenda.getAgendaForDay(it)
            })
        )
    }

    val agendaItemPopup = remember {
        mutableStateOf<AgendaItemWithAbsence?>(null)
    }

    val selectedWeek =
        remember { derivedStateOf { floor((dayPagerState.currentPage - (dayPagerState.pageCount / 2).toFloat()) / days.size).toInt() } }
    val firstDayOfSelectedWeek = remember {
        derivedStateOf {
            firstDayOfWeek.plus(
                selectedWeek.value * 7,
                DateTimeUnit.DAY
            )
        }
    }

    LaunchedEffect(selectedWeek.value) {
        val newAgenda =
            account.refreshAgenda(firstDayOfSelectedWeek.value, days.size - 1, selectedWeek.value)
                ?: loadedAgendas[selectedWeek.value + 100]
        if (newAgenda != null) {
            loadedAgendas[selectedWeek.value + 100] = newAgenda
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        DaySelector(
            dayPagerState = dayPagerState,
            days = days,
            selectedWeek = selectedWeek,
            firstDayOfWeek = firstDayOfWeek
        )

        Agenda(
            initialPage = initialPage,
            dayPagerState = dayPagerState,
            days = days,
            loadedAgendas = loadedAgendas,
            refresh = {
                scope.launch {
                    it.isRefreshing = true
                    loadedAgendas[selectedWeek.value + 100] = account.refreshAgenda(
                        firstDayOfSelectedWeek.value,
                        days.size - 1,
                        selectedWeek.value
                    ) ?: loadedAgendas[selectedWeek.value + 100] ?: listOf()
                    it.isRefreshing = false
                }
            },
            openAgendaItemWithAbsence = {
                agendaItemPopup.value = it
            }
        )
    }

    if (dayPagerState.currentPage != initialPage) {
        Box(Modifier.fillMaxSize()) {
            Button(
                onClick = { scope.launch { dayPagerState.animateScrollToPage(initialPage) } },
                modifier = Modifier
                    .size(60.dp)
                    .padding(10.dp)
                    .align(Alignment.BottomEnd),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Today",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AnimatedVisibility(
            visible = agendaItemPopup.value != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            val savedAgendaItem = remember {
                agendaItemPopup.value
            }
            AgendaItemPopup(componentContext, savedAgendaItem!!) {
                agendaItemPopup.value = null
            }
        }
    }
}

suspend fun MagisterAccount.refreshAgenda(
    start: LocalDate,
    totalDays: Int,
    selectedWeek: Int
): List<List<AgendaItemWithAbsence>>? {
    try {
        println("Refreshing agenda for week $selectedWeek")

        val end = start.plus(totalDays, DateTimeUnit.DAY)

        val timeTableWeek = getAbsences(
            accountId,
            tenantUrl,
            tokens.accessToken,
            "${start.year}-${start.month}-${start.dayOfMonth}",
            "${end.year}-${end.month}-${end.dayOfMonth}",
            getMagisterAgenda(
                accountId,
                tenantUrl,
                tokens.accessToken,
                start,
                end
            )
        )

        if (selectedWeek == 0) {
            println("Saving agenda for current week")
            agenda = timeTableWeek
        }

        return (0..totalDays).map { timeTableWeek.getAgendaForDay(it) }
    } catch (e: MagisterException) {
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}
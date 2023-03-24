package nl.tiebe.otarium.ui.screen.settings.popups

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.utils.ui.CBackHandler
import nl.tiebe.otarium.utils.ui.getLocalizedString
import kotlin.random.Random

@Composable
internal fun BugReportPopup(componentContext: ComponentContext, onExit: () -> Unit) {
    CBackHandler(componentContext, onBack = onExit)

    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp))
        {
            Text(
                text = getLocalizedString(MR.strings.bug_text_1),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Divider()

            Row(modifier = Modifier
                .fillMaxWidth(0.95f)
                .height(70.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Remove random grade",
                    textAlign = TextAlign.Center)
                Button(
                    onClick = {
                        val account = Data.selectedAccount
                        val savedGrades = account.fullGradeList.toMutableList()

                        savedGrades.removeAt(Random.nextInt(savedGrades.size))
                        account.fullGradeList = savedGrades

                    }) {
                    Text("remove")
                }
            }

            Divider()

            Text(
                text = getLocalizedString(MR.strings.bug_text_2),
                modifier = Modifier.padding(vertical = 16.dp))
        }
    }
}
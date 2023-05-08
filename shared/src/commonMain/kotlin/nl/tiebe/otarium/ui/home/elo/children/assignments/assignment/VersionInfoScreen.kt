package nl.tiebe.otarium.ui.home.elo.children.assignments.assignment

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.tiebe.magisterapi.response.assignment.Assignment
import kotlinx.datetime.toLocalDateTime
import nl.tiebe.otarium.ui.utils.parseHtml
import nl.tiebe.otarium.utils.toFormattedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun VersionInfoScreen(component: AssignmentScreenComponent, assignment: Assignment, versionId: Int, modifier: Modifier) {
    ElevatedCard(Modifier.then(modifier)) {
        Column {
            val version = component.versions.value.first { it.id == versionId }

            ListItem(
                overlineText = { Text("Title") },
                headlineText = { Text(assignment.title) }
            )

            ListItem(
                overlineText = { Text("Submit before") },
                headlineText = { Text(version.deadline.substring(0, 26).toLocalDateTime().toFormattedString()) }
            )


            ListItem(
                overlineText = { Text("Description") },
                headlineText = { Text(assignment.description.parseHtml()) }
            )
        }
    }
}
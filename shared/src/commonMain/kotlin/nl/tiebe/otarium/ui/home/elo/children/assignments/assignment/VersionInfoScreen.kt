package nl.tiebe.otarium.ui.home.elo.children.assignments.assignment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.tiebe.magisterapi.response.assignment.Assignment
import kotlinx.datetime.toLocalDateTime
import nl.tiebe.otarium.ui.utils.parseHtml
import nl.tiebe.otarium.utils.toFormattedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun VersionInfoScreen(component: AssignmentScreenComponent, assignment: Assignment, versionId: Int, modifier: Modifier) {
    val version = component.versions.value.first { it.id == versionId }


    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        ElevatedCard {
            Column {
                ListItem(
                    overlineText = { Text("Title") },
                    headlineText = { Text(assignment.title) }
                )

                ListItem(
                    overlineText = { Text("Version")},
                    headlineText = { Text(version.versionIndex.toString()) }
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

        Spacer(modifier = Modifier.height(10.dp))

        ElevatedCard {
            Column {
                if (assignment.gradedOn != null) {
                    ListItem(
                        overlineText = { Text("Graded") },
                        headlineText = { Text(assignment.gradedOn!!.substring(0, 26).toLocalDateTime().toFormattedString()) }
                    )
                }

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
}
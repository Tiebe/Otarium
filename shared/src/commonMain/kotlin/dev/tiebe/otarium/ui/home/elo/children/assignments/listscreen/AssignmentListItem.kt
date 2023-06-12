package dev.tiebe.otarium.ui.home.elo.children.assignments.listscreen

import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.tiebe.magisterapi.response.assignment.Assignment
import dev.tiebe.otarium.logic.root.home.children.elo.children.assignments.children.list.AssignmentListComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AssignmentListItem(component: AssignmentListComponent, item: Assignment) {
    ListItem(
        headlineText = { Text(item.title) },
        modifier = Modifier.clickable { component.navigateToAssignment(item) },
        trailingContent = { Text(item.grade)
        }
    )
}
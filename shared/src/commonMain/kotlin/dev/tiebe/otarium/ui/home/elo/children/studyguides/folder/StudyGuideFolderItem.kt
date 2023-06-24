package dev.tiebe.otarium.ui.home.elo.children.studyguides.folder

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.tiebe.magisterapi.response.studyguide.StudyGuideContentItem
import dev.tiebe.otarium.logic.root.home.children.elo.children.studyguides.children.folder.StudyGuideFolderComponent
import dev.tiebe.otarium.ui.utils.HtmlView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StudyGuideFolderItem(component: StudyGuideFolderComponent, item: StudyGuideContentItem) {
    var showContents by remember { mutableStateOf(false) }

    ListItem(
        headlineText = { Text(item.title) },
        supportingText = { HtmlView(item.description, maxLines = 1) },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface
        ),
        modifier = Modifier.clickable { showContents = !showContents }
    )
    Divider()

    AnimatedVisibility(visible = showContents, enter = expandVertically(), exit = shrinkVertically()) {
        Column(modifier = Modifier.padding(start = 16.dp)) {
            item.resources.forEach {
                StudyGuideResourceListItem(component, it)

                if (item.resources.last() != it) {
                    Divider()
                }
            }
        }
    }

}
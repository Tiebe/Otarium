package nl.tiebe.otarium.ui.home.elo.children.studyguides.folder

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import dev.tiebe.magisterapi.response.studyguide.StudyGuideContentItem
import nl.tiebe.otarium.logic.root.home.children.elo.children.studyguides.children.folder.StudyGuideFolderComponent
import nl.tiebe.otarium.ui.utils.HtmlView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StudyGuideFolderItem(component: StudyGuideFolderComponent, item: StudyGuideContentItem) {
    var showContents by remember { mutableStateOf(false) }

    ListItem(
        headlineContent = { Text(item.title) },
        overlineContent = { HtmlView(
            item.description,
            maxLines = 1,
            backgroundColor = ListItemDefaults.containerColor.toArgb(),
            onClick = { showContents = !showContents }
        ) },
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
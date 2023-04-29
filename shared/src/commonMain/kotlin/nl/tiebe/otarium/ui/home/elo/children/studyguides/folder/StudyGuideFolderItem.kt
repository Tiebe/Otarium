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
import androidx.compose.ui.unit.dp
import dev.tiebe.magisterapi.response.studyguide.StudyGuideContentItem
import nl.tiebe.otarium.ui.utils.ClickableText
import nl.tiebe.otarium.ui.utils.parseHtml

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StudyGuideFolderItem(component: StudyGuideFolderComponent, item: StudyGuideContentItem) {
    var showContents by remember { mutableStateOf(false) }

    ListItem(
        headlineText = { Text(item.title) },
        supportingText = { ClickableText(item.description.parseHtml()) },
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
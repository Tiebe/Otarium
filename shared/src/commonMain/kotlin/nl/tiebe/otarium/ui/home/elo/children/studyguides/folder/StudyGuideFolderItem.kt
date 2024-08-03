package nl.tiebe.otarium.ui.home.elo.children.studyguides.folder

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import dev.tiebe.magisterapi.response.studyguide.StudyGuideContentItem
import nl.tiebe.otarium.logic.root.home.children.elo.children.studyguides.children.folder.StudyGuideFolderComponent

@Composable
internal fun StudyGuideFolderItem(component: StudyGuideFolderComponent, item: StudyGuideContentItem) {
    var showContents by remember { mutableStateOf(false) }

    ListItem(
        headlineContent = { Text(item.title) },
        overlineContent = {
            val state = rememberRichTextState()

            LaunchedEffect(item) {
                state.setHtml(item.description)
            }

            RichText(
                state,
                maxLines = 1,
                modifier = Modifier.clickable { showContents = !showContents }
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface
        ),
        modifier = Modifier.clickable { showContents = !showContents }
    )
    HorizontalDivider()

    AnimatedVisibility(visible = showContents, enter = expandVertically(), exit = shrinkVertically()) {
        Column(modifier = Modifier.padding(start = 16.dp)) {
            item.resources.forEach {
                StudyGuideResourceListItem(component, it)

                if (item.resources.last() != it) {
                    HorizontalDivider()
                }
            }
        }
    }

}
package nl.tiebe.otarium.androidApp.ui.home.elo.children.studyguides.folder

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.tiebe.magisterapi.response.studyguide.Resource
import nl.tiebe.otarium.androidApp.ui.utils.DownloadingFileIndicator
import nl.tiebe.otarium.androidApp.ui.utils.LoadingFileIndicator
import nl.tiebe.otarium.logic.root.home.children.elo.children.studyguides.children.folder.StudyGuideFolderComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StudyGuideResourceListItem(component: StudyGuideFolderComponent, item: Resource) {
    Box {
        ListItem(
            modifier = Modifier.clickable { component.downloadResource(item) },
            headlineText = { Text(item.name) },
        )

        val progress =
            component.resourceDownloadProgress.subscribeAsState().value[item.id] ?: 0f

        if (progress != 0f && !progress.isNaN()) {
            val color = MaterialTheme.colorScheme.primary
            val trackColor = MaterialTheme.colorScheme.surfaceVariant

            if (progress != 1f) {
                DownloadingFileIndicator(
                    progress = progress,
                    modifier = Modifier.matchParentSize().align(Alignment.BottomStart),
                    color = color,
                    trackColor = trackColor,
                    height = 5.dp
                )
            } else {
                LoadingFileIndicator(
                    modifier = Modifier.matchParentSize().align(Alignment.BottomStart),
                    color = color,
                    trackColor = trackColor,
                    height = 5.dp
                )
            }

        }
    }
}
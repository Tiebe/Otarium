package nl.tiebe.otarium.ui.home.elo.children.studyguides.folder

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
import nl.tiebe.otarium.logic.root.home.children.elo.children.studyguides.children.folder.StudyGuideFolderComponent
import nl.tiebe.otarium.ui.utils.DownloadIndicator
import nl.tiebe.otarium.ui.utils.DownloadingFileIndicator
import nl.tiebe.otarium.ui.utils.LoadingFileIndicator

@Composable
internal fun StudyGuideResourceListItem(component: StudyGuideFolderComponent, item: Resource) {
    Box {
        ListItem(
            modifier = Modifier.clickable { component.downloadResource(item) },
            headlineContent = { Text(item.name) },
        )

        val progress =
            component.resourceDownloadProgress.subscribeAsState().value[item.id] ?: 0f

        DownloadIndicator(progress)
    }
}
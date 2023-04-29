package nl.tiebe.otarium.ui.home.elo.children.studyguides.folder

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.tiebe.magisterapi.response.studyguide.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StudyGuideResourceListItem(component: StudyGuideFolderComponent, item: Resource) {
    ListItem(
        headlineText = { Text(item.name) },
    )
}
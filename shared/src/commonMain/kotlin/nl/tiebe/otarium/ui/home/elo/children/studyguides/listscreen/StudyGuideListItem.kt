package nl.tiebe.otarium.ui.home.elo.children.studyguides.listscreen

import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.tiebe.magisterapi.response.studyguide.StudyGuide
import nl.tiebe.otarium.logic.root.home.children.elo.children.studyguides.children.list.StudyGuideListComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StudyGuideListItem(component: StudyGuideListComponent, item: StudyGuide) {
    ListItem(
        headlineContent = { Text(item.title) },
        modifier = Modifier.clickable { component.navigateToStudyGuide(item) }
    )
}
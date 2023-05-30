package nl.tiebe.otarium.ui.home.elo.children.studyguides.listscreen

import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.tiebe.magisterapi.response.studyguide.StudyGuide

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyGuideListItem(component: StudyGuideListComponent, item: StudyGuide) {
    ListItem(
        headlineText = { Text(item.title) },
        modifier = Modifier.clickable { component.navigateToStudyGuide(item) }
    )
}
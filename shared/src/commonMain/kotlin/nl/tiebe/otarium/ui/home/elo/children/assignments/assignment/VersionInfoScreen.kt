package nl.tiebe.otarium.ui.home.elo.children.assignments.assignment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.tiebe.magisterapi.response.assignment.Assignment

@Composable
internal fun VersionInfoScreen(component: AssignmentScreenComponent, assignment: Assignment, versionId: Int, modifier: Modifier) {
    val version = component.versions.value.first { it.id == versionId }

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        MainInfoCard(assignment, version)

        if (version.gradedOn != null || version.grade != null || version.teacherNote != null || version.feedbackAttachments.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))

            TeacherFeedbackCard(component, assignment, version)
        }

        if (version.submittedOn != null || version.studentNote != null || version.studentAttachments.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))

            StudentVersionCard(component, assignment, version)
        }
    }
}
package nl.tiebe.otarium.ui.home.elo.children.assignments.assignment

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.tiebe.magisterapi.response.assignment.Assignment
import dev.tiebe.magisterapi.response.assignment.AssignmentVersion
import kotlinx.datetime.toLocalDateTime
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.logic.root.home.children.elo.children.assignments.children.assignment.AssignmentScreenComponent
import nl.tiebe.otarium.ui.utils.DownloadIndicator
import nl.tiebe.otarium.ui.utils.HtmlView
import nl.tiebe.otarium.utils.OtariumIcons
import nl.tiebe.otarium.utils.otariumicons.Email
import nl.tiebe.otarium.utils.otariumicons.email.Attachment
import nl.tiebe.otarium.utils.toFormattedString
import nl.tiebe.otarium.utils.ui.getLocalizedString


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainInfoCard(assignment: Assignment, version: AssignmentVersion) {
    ElevatedCard {
        Column {
            ListItem(
                overlineContent = { Text(getLocalizedString(MR.strings.assignment_title)) },
                headlineContent = { Text(assignment.title) }
            )

            ListItem(
                overlineContent = { Text(getLocalizedString(MR.strings.assignment_version)) },
                headlineContent = { Text(version.versionIndex.toString()) }
            )

            ListItem(
                overlineContent = { Text(getLocalizedString(MR.strings.assignment_deadline)) },
                headlineContent = { Text(version.deadline.substring(0, 26).toLocalDateTime().toFormattedString()) }
            )


            ListItem(
                overlineContent = { Text(getLocalizedString(MR.strings.assignment_description)) },
                headlineContent = { HtmlView(assignment.description, maxLines = 1, backgroundColor = ListItemDefaults.containerColor.toArgb()) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TeacherFeedbackCard(component: AssignmentScreenComponent, version: AssignmentVersion) {
    ElevatedCard {
        Column {
            if (version.gradedOn != null) {
                ListItem(
                    overlineContent = { Text(getLocalizedString(MR.strings.assignment_graded_on)) },
                    headlineContent = { Text(version.gradedOn!!.substring(0, 26).toLocalDateTime().toFormattedString()) }
                )
            }

            if (version.grade != null) {
                ListItem(
                    overlineContent = { Text(getLocalizedString(MR.strings.assignment_grade)) },
                    headlineContent = { Text(version.grade!!) }
                )
            }

            if (version.teacherNote != null) {
                ListItem(
                    overlineContent = { Text(getLocalizedString(MR.strings.assignment_feedback)) },
                    headlineContent = { HtmlView(version.teacherNote!!, maxLines = 1, backgroundColor = ListItemDefaults.containerColor.toArgb()) }
                )
            }

            if (version.feedbackAttachments.isNotEmpty()) {
                val scrollState = rememberScrollState()

                Row(modifier = Modifier.fillMaxWidth().horizontalScroll(scrollState)) {
                    for (attachment in version.feedbackAttachments) {
                        ElevatedCard(
                            onClick = { component.downloadAttachment(attachment) },
                            modifier = Modifier.height(70.dp).padding(10.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Row(
                                    modifier = Modifier.fillMaxSize().padding(10.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        OtariumIcons.Email.Attachment,
                                        contentDescription = "Attachment"
                                    )
                                    Text(text = attachment.naam, modifier = Modifier.padding(start = 10.dp))
                                }

                                DownloadIndicator(
                                    component.attachmentDownloadProgress.subscribeAsState().value[attachment.id]
                                        ?: 0f
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StudentVersionCard(component: AssignmentScreenComponent, version: AssignmentVersion) {
    ElevatedCard {
        Column {
            if (version.submittedOn != null) {
                ListItem(
                    overlineContent = { Text(getLocalizedString(MR.strings.assignment_submitted_on)) },
                    headlineContent = { Text(version.submittedOn!!.substring(0, 26).toLocalDateTime().toFormattedString()) }
                )
            }

            if (version.studentNote != null) {
                ListItem(
                    overlineContent = { Text(getLocalizedString(MR.strings.assignment_description)) },
                    headlineContent = { HtmlView(version.studentNote!!, maxLines = 1, backgroundColor = ListItemDefaults.containerColor.toArgb()) }
                )
            }

            if (version.studentAttachments.isNotEmpty()) {
                val scrollState = rememberScrollState()

                Row(modifier = Modifier.fillMaxWidth().horizontalScroll(scrollState)) {
                    for (attachment in version.studentAttachments) {
                        ElevatedCard(
                            onClick = { component.downloadAttachment(attachment) },
                            modifier = Modifier.height(70.dp).padding(10.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Row(
                                    modifier = Modifier.fillMaxSize().padding(10.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        OtariumIcons.Email.Attachment,
                                        contentDescription = "Attachment"
                                    )
                                    Text(text = attachment.naam, modifier = Modifier.padding(start = 10.dp))
                                }

                                DownloadIndicator(
                                    component.attachmentDownloadProgress.subscribeAsState().value[attachment.id]
                                        ?: 0f
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
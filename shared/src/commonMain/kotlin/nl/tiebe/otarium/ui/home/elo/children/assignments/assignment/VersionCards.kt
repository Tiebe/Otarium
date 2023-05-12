package nl.tiebe.otarium.ui.home.elo.children.assignments.assignment

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.tiebe.magisterapi.response.assignment.Assignment
import dev.tiebe.magisterapi.response.assignment.AssignmentVersion
import kotlinx.datetime.toLocalDateTime
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.ui.utils.DownloadIndicator
import nl.tiebe.otarium.ui.utils.parseHtml
import nl.tiebe.otarium.utils.otariumicons.Email
import nl.tiebe.otarium.utils.OtariumIcons
import nl.tiebe.otarium.utils.otariumicons.email.Attachment
import nl.tiebe.otarium.utils.toFormattedString
import nl.tiebe.otarium.utils.ui.getLocalizedString


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainInfoCard(assignment: Assignment, version: AssignmentVersion) {
    ElevatedCard {
        Column {
            ListItem(
                overlineText = { Text(getLocalizedString(MR.strings.assignment_title)) },
                headlineText = { Text(assignment.title) }
            )

            ListItem(
                overlineText = { Text(getLocalizedString(MR.strings.assignment_version)) },
                headlineText = { Text(version.versionIndex.toString()) }
            )

            ListItem(
                overlineText = { Text(getLocalizedString(MR.strings.assignment_deadline)) },
                headlineText = { Text(version.deadline.substring(0, 26).toLocalDateTime().toFormattedString()) }
            )


            ListItem(
                overlineText = { Text(getLocalizedString(MR.strings.assignment_description)) },
                headlineText = { Text(assignment.description.parseHtml()) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TeacherFeedbackCard(component: AssignmentScreenComponent, assignment: Assignment, version: AssignmentVersion) {
    ElevatedCard {
        Column {
            if (version.gradedOn != null) {
                ListItem(
                    overlineText = { Text(getLocalizedString(MR.strings.assignment_graded_on)) },
                    headlineText = { Text(version.gradedOn!!.substring(0, 26).toLocalDateTime().toFormattedString()) }
                )
            }

            if (version.grade != null) {
                ListItem(
                    overlineText = { Text(getLocalizedString(MR.strings.assignment_grade)) },
                    headlineText = { Text(version.grade!!) }
                )
            }

            if (version.teacherNote != null) {
                ListItem(
                    overlineText = { Text(getLocalizedString(MR.strings.assignment_feedback)) },
                    headlineText = { Text(version.teacherNote!!.parseHtml()) }
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
internal fun StudentVersionCard(component: AssignmentScreenComponent, assignment: Assignment, version: AssignmentVersion) {
    ElevatedCard {
        Column {
            if (version.submittedOn != null) {
                ListItem(
                    overlineText = { Text(getLocalizedString(MR.strings.assignment_submitted_on)) },
                    headlineText = { Text(version.submittedOn!!.substring(0, 26).toLocalDateTime().toFormattedString()) }
                )
            }

            if (version.studentNote != null) {
                ListItem(
                    overlineText = { Text(getLocalizedString(MR.strings.assignment_description)) },
                    headlineText = { Text(version.studentNote!!.parseHtml()) }
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
package dev.tiebe.otarium.logic.root.home.children.elo.children.assignments.children.assignment

import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.response.assignment.Assignment
import dev.tiebe.magisterapi.response.assignment.AssignmentVersion
import dev.tiebe.magisterapi.response.assignment.FeedbackBijlagen
import dev.tiebe.magisterapi.response.assignment.LeerlingBijlagen

interface AssignmentScreenComponent {
    val assignment: Value<Assignment>
    val versions: Value<List<AssignmentVersion>>

    val assignmentLink: String

    val isRefreshing: Value<Boolean>

    fun refreshAssignment()

    suspend fun getVersions(assignment: Assignment)
    fun downloadAttachment(attachment: FeedbackBijlagen)

    val attachmentDownloadProgress: Value<Map<Int, Float>>
    fun downloadAttachment(attachment: LeerlingBijlagen)
}
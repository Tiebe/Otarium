package nl.tiebe.otarium.logic.home.children.elo.children.assignments.children.assignment

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

/**
 * The interface for the assignment information screen.
 *
 * @param Assignment The type of assignment.
 */
interface AssignmentScreenComponent<Assignment: Any, AssignmentVersion, Attachment> {
    /** The assignment. */
    val assignment: MutableValue<Assignment>

    /** The assignment versions. */
    val versions: MutableValue<List<AssignmentVersion>>

    /**
     * Refresh the assignment.
     *
     * @return The refreshed assignment. This should also be set in the [assignment] value.
     */
    fun refreshAssignment(): Assignment

    /**
     * Refresh the assignment versions.
     *
     * @return The refreshed assignment versions. This should also be set in the [versions] value.
     */
    suspend fun getVersions(assignment: Assignment): List<AssignmentVersion>

    /**
     * Download the given attachment. The progress should be set in the [attachmentDownloadProgress] value.
     *
     * @param attachment The attachment to download.
     */
    fun downloadAttachment(attachment: Attachment)

    /** The download progress of the attachments. */
    val attachmentDownloadProgress: Value<Map<Int, Float>>
}
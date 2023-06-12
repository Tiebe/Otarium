package dev.tiebe.otarium.ui.home.elo.children.assignments.assignment

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.tiebe.magisterapi.api.assignment.AssignmentFlow
import dev.tiebe.magisterapi.response.assignment.Assignment
import dev.tiebe.magisterapi.response.assignment.AssignmentVersion
import dev.tiebe.magisterapi.response.assignment.FeedbackBijlagen
import dev.tiebe.magisterapi.response.assignment.LeerlingBijlagen
import io.ktor.client.statement.readBytes
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.http.appendEncodedPathSegments
import kotlinx.coroutines.launch
import dev.tiebe.otarium.Data
import dev.tiebe.otarium.ui.root.componentCoroutineScope
import dev.tiebe.otarium.utils.openFileFromCache
import dev.tiebe.otarium.utils.requestGET
import dev.tiebe.otarium.utils.writeFile

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

class DefaultAssignmentScreenComponent(componentContext: ComponentContext, override val assignmentLink: String): AssignmentScreenComponent, ComponentContext by componentContext {
    override val assignment: MutableValue<Assignment> = MutableValue(Assignment(false, null, "", listOf(), null, 0, null, "", 0, listOf(), false, "", false, 0, "", "", listOf(), false))
    override val versions: MutableValue<List<AssignmentVersion>> = MutableValue(listOf())

    override val isRefreshing: MutableValue<Boolean> = MutableValue(false)
    val scope = componentCoroutineScope()

    override fun refreshAssignment() {
        scope.launch {
            isRefreshing.value = true
            val tempAssignment = AssignmentFlow.getFullAssignment(Url(Data.selectedAccount.tenantUrl), Data.selectedAccount.tokens.accessToken, assignmentLink)
            getVersions(tempAssignment)
            assignment.value = tempAssignment
            isRefreshing.value = false
        }
    }

    override suspend fun getVersions(assignment: Assignment) {
        val list = mutableListOf<AssignmentVersion>()

        assignment.navigationItemsVersion.forEach {
            list.add(
                AssignmentFlow.getVersionInfo(
                    Url(Data.selectedAccount.tenantUrl),
                    Data.selectedAccount.tokens.accessToken,
                    it.links.first { link -> link.rel == "Self" }.href
                )
            )
        }

        versions.value = list
    }

    override val attachmentDownloadProgress: MutableValue<Map<Int, Float>> = MutableValue(mapOf())

    override fun downloadAttachment(attachment: FeedbackBijlagen) {
        scope.launch {
            val response = requestGET(
                URLBuilder(Data.selectedAccount.tenantUrl).appendEncodedPathSegments(attachment.links.first { it.rel == "Self" }.href).build(),
                accessToken = Data.selectedAccount.tokens.accessToken,
                onDownload = { bytesSentTotal, contentLength ->
                    attachmentDownloadProgress.value = attachmentDownloadProgress.value + Pair(attachment.id, bytesSentTotal.toFloat() / contentLength.toFloat())
                }
            ).readBytes()

            writeFile(attachment.id.toString(), attachment.naam, response)
            openFileFromCache(attachment.id.toString(), attachment.naam)

            attachmentDownloadProgress.value = attachmentDownloadProgress.value.toMutableMap().also {
                it.remove(attachment.id)
            }
        }
    }

    override fun downloadAttachment(attachment: LeerlingBijlagen) {
        scope.launch {
            val response = requestGET(
                URLBuilder(Data.selectedAccount.tenantUrl).appendEncodedPathSegments(attachment.links.first { it.rel == "Self" }.href).build(),
                accessToken = Data.selectedAccount.tokens.accessToken,
                onDownload = { bytesSentTotal, contentLength ->
                    attachmentDownloadProgress.value = attachmentDownloadProgress.value + Pair(attachment.id, bytesSentTotal.toFloat() / contentLength.toFloat())
                }
            ).readBytes()

            writeFile(attachment.id.toString(), attachment.naam, response)
            openFileFromCache(attachment.id.toString(), attachment.naam)

            attachmentDownloadProgress.value = attachmentDownloadProgress.value.toMutableMap().also {
                it.remove(attachment.id)
            }
        }
    }


    init {
        refreshAssignment()
    }


}
package nl.tiebe.otarium.logic.data.wrapper

import com.arkivanov.essenty.parcelable.Parcelable
import nl.tiebe.otarium.logic.data.wrapper.messages.MessageItem

interface Account : Parcelable {
    val id: Int

    var agenda: List<FullTimetableItem>
    var grades: List<RecentGrade>
    var messages: List<MessageItem>
    var fullGradeList: List<nl.tiebe.otarium.logic.magister.data.GradeWithGradeInfo>
}
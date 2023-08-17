package nl.tiebe.otarium.logic.magister.data.wrapper

import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
class Subject(private val subject: MagisterRecentGrade) : SubjectWrapper {
    override val id: Int get() = subject.gradeColumnId

}
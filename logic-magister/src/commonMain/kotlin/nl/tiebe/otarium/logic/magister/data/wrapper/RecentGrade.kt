package nl.tiebe.otarium.logic.magister.data.wrapper

import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
class RecentGrade(private val recentGrade: MagisterRecentGrade) : RecentGradeWrapper {
    override val id: Int get() = recentGrade.gradeColumnId

}
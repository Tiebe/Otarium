package nl.tiebe.openbaarlyceumzeist.database.grade

import io.realm.kotlin.types.RealmObject
import nl.tiebe.magisterapi.response.grades.Study
import nl.tiebe.magisterapi.response.grades.Year


class YearEntity : RealmObject {
    companion object {
        fun fromYear(year: Year): YearEntity {
            return YearEntity().apply {
                this._id = year.id
                this.study = year.study
                this.group = GroupEntity.fromGroup(year.group)
                this.lessonSemester = LessonSemesterEntity.fromLessonSemester(year.lessonSemester)
                this.profiles = year.profiles
                this.personalMentor = year.personalMentor
                this.start = year.start
                this.end = year.end
                this.mainRegistration = year.mainRegistration
            }
        }
    }

    var _id: Int? = null
    var study: Study? = null
    var group: GroupEntity? = null
    var lessonSemester: LessonSemesterEntity? = null
    var profiles: List<Any>? = null
    var personalMentor: Any? = null
    var start: String? = null
    var end: String? = null
    var mainRegistration: Boolean? = null
}
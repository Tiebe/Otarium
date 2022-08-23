package nl.tiebe.openbaarlyceumzeist.database.grade

import io.realm.kotlin.types.RealmObject
import nl.tiebe.magisterapi.response.grades.Grade


@Suppress("PropertyName")
class GradeEntity : RealmObject {
    companion object {
        fun fromGrade(grade: Grade): GradeEntity {
            return GradeEntity().apply {
                this._id = grade.id
                this.grade = grade.grade
                this.isSufficient = grade.isSufficient
                this.enteredBy = grade.enteredBy
                this.dateEntered = grade.dateEntered
                this.gradeSemester = GradeSemesterEntity.fromGradeSemester(
                    grade.gradeSemester
                )
                this.subject = SubjectEntity.fromSubject(grade.subject)
                this.catchUp = grade.catchUp
                this.exemption = grade.exemption
                this.counts = grade.counts
                this.gradeColumn =
                    GradeColumnEntity.fromGradeColumn(grade.gradeColumn)
                this.gradeInfoIdEloExercise = grade.gradeInfoIdEloExercise
                this.teacher = grade.teacher
                this.subjectExemption = grade.subjectExemption
                this.subjectExemption2 = grade.subjectExemption2
                this.year = YearEntity.fromYear(grade.year)
            }
        }
    }

    var _id: Int = 0
    var grade: String = ""
    var isSufficient: Boolean = false
    var enteredBy: String = ""
    var dateEntered: String = ""
    var gradeSemester: GradeSemesterEntity? = GradeSemesterEntity()
    var subject: SubjectEntity? = SubjectEntity()
    var catchUp: Boolean = false
    var exemption: Boolean = false
    var counts: Boolean = false
    var gradeColumn: GradeColumnEntity? = GradeColumnEntity()
    var gradeInfoIdEloExercise: Int = 0
    var teacher: String = ""
    var subjectExemption: Boolean = false
    var subjectExemption2: Boolean = false
    var year: YearEntity? = YearEntity()
}
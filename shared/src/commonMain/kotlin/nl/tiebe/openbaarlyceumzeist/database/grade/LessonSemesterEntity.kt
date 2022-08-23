package nl.tiebe.openbaarlyceumzeist.database.grade

import io.realm.kotlin.types.RealmObject
import nl.tiebe.magisterapi.response.grades.LessonSemester

class LessonSemesterEntity : RealmObject {
    companion object {
        fun fromLessonSemester(lessonSemester: LessonSemester): LessonSemesterEntity {
            return LessonSemesterEntity().apply {
                this.code = lessonSemester.code
            }
        }
    }

    var code: String = ""

}
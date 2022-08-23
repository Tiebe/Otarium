package nl.tiebe.openbaarlyceumzeist.database.grade

import io.realm.kotlin.types.RealmObject
import nl.tiebe.magisterapi.response.grades.Grade

class GradeSemesterEntity : RealmObject {
    companion object {
        fun fromGradeSemester(gradeSemester: Grade.GradeSemester): GradeSemesterEntity {
            return GradeSemesterEntity().apply {
                _id = gradeSemester.id
                name = gradeSemester.name
                index = gradeSemester.index
            }
        }
    }

    var _id: Int? = 0
    var name: String? = ""
    var index: Int? = 0
}
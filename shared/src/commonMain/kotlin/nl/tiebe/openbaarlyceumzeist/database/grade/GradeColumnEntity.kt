package nl.tiebe.openbaarlyceumzeist.database.grade

import io.realm.kotlin.types.RealmObject
import nl.tiebe.magisterapi.response.grades.GradeColumn

class GradeColumnEntity : RealmObject {
    companion object {
        fun fromGradeColumn(gradeColumn: GradeColumn): GradeColumnEntity {
            return GradeColumnEntity().apply {
                _id = gradeColumn.id
                name = gradeColumn.name
                number = gradeColumn.number
                index = gradeColumn.index
                heading = gradeColumn.heading
                description = gradeColumn.description
                type = gradeColumn.type
                isCatchUpColumn = gradeColumn.isCatchUpColumn
            }
        }
    }

    var _id: Int = 0
    var name: String? = ""
    var number: String? = ""
    var index: String? = ""
    var heading: String? = ""
    var description: String? = ""
    var type: Int? = 0
    var isCatchUpColumn: Boolean? = false
}
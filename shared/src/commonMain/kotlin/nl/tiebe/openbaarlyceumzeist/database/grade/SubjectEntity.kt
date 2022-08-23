package nl.tiebe.openbaarlyceumzeist.database.grade

import io.realm.kotlin.types.RealmObject
import nl.tiebe.magisterapi.response.grades.Subject

class SubjectEntity : RealmObject {
    companion object {
        fun fromSubject(subject: Subject): SubjectEntity {
            return SubjectEntity().apply {
                _id = subject.id
                abbreviation = subject.abbreviation
                description = subject.description
                grade = subject.index
            }
        }
    }

    var _id: Int = 0
    var abbreviation: String = ""
    var description: String = ""
    var grade: Int = 0


}
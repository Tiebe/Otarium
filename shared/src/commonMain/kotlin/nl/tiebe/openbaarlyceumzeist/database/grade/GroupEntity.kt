package nl.tiebe.openbaarlyceumzeist.database.grade

import io.realm.kotlin.types.RealmObject
import nl.tiebe.magisterapi.response.grades.Group

class GroupEntity : RealmObject {
    companion object {
        fun fromGroup(group: Group): GroupEntity {
            return GroupEntity().apply {
                this._id = group.id
                this.code = group.code
                this.description = group.description
            }
        }
    }

    var _id: Int = 0
    var code: String = ""
    var description: String = ""
}
package nl.tiebe.openbaarlyceumzeist.database

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import nl.tiebe.magisterapi.response.grades.Grade
import nl.tiebe.openbaarlyceumzeist.database.grade.*

class Database {
    private lateinit var realm: Realm

    fun addGrade(grade: Grade) {
        realm.writeBlocking {
            copyToRealm(GradeEntity.fromGrade(grade))
        }
    }

    fun checkOrAddGrade(grade: Grade): Boolean {
        val gradeEntity = realm.query<GradeEntity>("_id == $0", grade.id).find()
        if (gradeEntity.isEmpty()) {
            addGrade(grade)
            return true
        }
        return false
    }

    fun removeRandomGrade() {
        val gradeEntities = realm.query<GradeEntity>().find()

        //random from list
        gradeEntities.random().let {
            realm.writeBlocking {
                findLatest(it)?.let { it1 -> delete(it1) }
            }
        }
    }


    fun setupDatabase(): Database {
        val config = RealmConfiguration.Builder(
            schema = setOf(
                GradeEntity::class,
                GradeColumnEntity::class,
                GradeSemesterEntity::class,
                SubjectEntity::class,
                LessonSemesterEntity::class,
                GroupEntity::class,
                YearEntity::class
            )
        )
            .schemaVersion(1)
            .migration(DatabaseMigration())
            .build()
        realm = Realm.open(config)
        return this
    }


    fun clearDatabase() {
        realm.writeBlocking {
            delete(query<GradeEntity>().find())
        }
    }

    fun getGrades(): List<GradeEntity> {
        return realm.query<GradeEntity>().find()
    }

}
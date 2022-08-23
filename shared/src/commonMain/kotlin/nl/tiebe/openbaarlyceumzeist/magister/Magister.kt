package nl.tiebe.openbaarlyceumzeist.magister

import io.github.aakira.napier.Napier
import nl.tiebe.openbaarlyceumzeist.*


object Magister {
    suspend fun onFirstSignIn() {
        Napier.d(tag = "Magister", message = "Clearing database")
        database.clearDatabase()

        Napier.d(tag = "Magister", message = "Updating data")
        account.updateData()
        for (grade in account.grades) {
            database.addGrade(grade)
        }

    }
}
package nl.tiebe.openbaarlyceumzeist.utils

import nl.tiebe.magisterapi.response.grades.Grade
import nl.tiebe.openbaarlyceumzeist.account

object Grade {
    fun getAverageGrade(gradeAbbreviation: String): Double {
        val gradeMap = HashMap<String, ArrayList<Grade>>()
        for (grade in account.grades) {
            if (grade.year.id == account.schoolYears[0].id) {
                if (gradeMap.containsKey(grade.subject.abbreviation)) {
                    gradeMap[grade.subject.abbreviation]?.add(grade)
                } else {
                    gradeMap[grade.subject.abbreviation] = arrayListOf(grade)
                }
            }
        }

        for (grade in gradeMap[gradeAbbreviation] ?: arrayListOf()) {
            println(grade.gradeColumn.name)
            println(grade.grade)
        }

        return 0.0
    }
}
package nl.tiebe.otarium

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.tiebe.magisterapi.response.general.year.grades.RecentGrade
import nl.tiebe.otarium.magister.AgendaItemWithAbsence
import nl.tiebe.otarium.magister.GradeWithGradeInfo

object Data {
    object Onboarding {
        fun finishOnboarding() {
            settings.putBoolean("finished_onboarding", true)
        }

        fun isFinishedOnboarding(): Boolean {
            return settings.getBoolean("finished_onboarding", false)
        }

        fun storeBypass(): Boolean {
            return settings.getBoolean("bypass", false)
        }

        fun bypassStore(bypass: Boolean) {
            settings.putBoolean("bypass", bypass)
        }
    }

    object Ads {
        fun showAds(): Boolean {
            return settings.getBoolean("show_ads", false)
        }

        fun showAds(show: Boolean) {
            settings.putBoolean("show_ads", show)
        }

        fun ageOfConsent(): Boolean {
            return settings.getBoolean("age_of_consent", false)
        }

        fun ageOfConsent(above: Boolean) {
            settings.putBoolean("age_of_consent", above)
        }
    }

    object Magister {
        object Grades {
            fun getSavedGrades(): List<RecentGrade> {
                return settings.getStringOrNull("grades")?.let { Json.decodeFromString(it) } ?: emptyList()
            }

            fun saveGrades(grades: List<RecentGrade>) {
                settings.putString("grades", Json.encodeToString(grades))
            }

            fun getSavedFullGradeList(): List<GradeWithGradeInfo> {
                return Json.decodeFromString(settings.getString("full_grade_list", "[]"))
            }

            fun saveFullGradeList(gradeList: List<GradeWithGradeInfo>) {
                settings.putString("full_grade_list", Json.encodeToString(gradeList))
            }
        }

        object Agenda {
            fun getSavedAgenda(): List<AgendaItemWithAbsence> {
                return settings.getStringOrNull("agenda")?.let { Json.decodeFromString(it) } ?: emptyList()
            }

            fun saveAgenda(agenda: List<AgendaItemWithAbsence>) {
                settings.putString("agenda", Json.encodeToString(agenda))
            }
        }
    }

}


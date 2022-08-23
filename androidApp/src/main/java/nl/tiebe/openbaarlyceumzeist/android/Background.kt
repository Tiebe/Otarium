package nl.tiebe.openbaarlyceumzeist.android

import android.content.Context
import androidx.work.*
import nl.tiebe.openbaarlyceumzeist.database
import nl.tiebe.openbaarlyceumzeist.database.Database
import nl.tiebe.openbaarlyceumzeist.isDatabaseInitialized
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class Background {
    companion object {
        fun getGradeIcon(gradeString: String): Int {
            val grade = gradeString.replace(",", ".").toDoubleOrNull() ?: return R.drawable.ic_grade

            return when (grade.roundToInt()) {
                1 -> R.drawable.ic_grade_1
                2 -> R.drawable.ic_grade_2
                3 -> R.drawable.ic_grade_3
                4 -> R.drawable.ic_grade_4
                5 -> R.drawable.ic_grade_5
                6 -> R.drawable.ic_grade_6
                7 -> R.drawable.ic_grade_7
                8 -> R.drawable.ic_grade_8
                9 -> R.drawable.ic_grade_9
                10 -> R.drawable.ic_grade_10
                else -> R.drawable.ic_grade
            }
        }
    }

    fun updatePeriodically(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<BackgroundWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "background",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

    class BackgroundWorker(appContext: Context, workerParams: WorkerParameters) :
        CoroutineWorker(appContext, workerParams) {
        override suspend fun doWork(): Result {
            if (!isDatabaseInitialized()) {
                database = Database().setupDatabase()
            }
/*
            if (Tokens().getPastTokens() != null) {
                account.updateData()
                Grade.getAverageGrade("la")
                for (grade in account.grades) {
                    if (database.checkOrAddGrade(grade)) {
                        //new grade added
                        val gradeInfo = GradeFlow().getGradeInfo(grade)

                        val notification = Notification.Builder(applicationContext, "grades")
                            .setSmallIcon(getGradeIcon(grade.grade))
                            .setContentTitle("Nieuw cijfer voor ${grade.subject.description}!")
                            .setContentText("Je hebt een ${grade.grade} gehaald voor ${gradeInfo.columnDescription}!")
                            .setGroup(System.currentTimeMillis().toString())
                            .build()

                        with(NotificationManagerCompat.from(applicationContext)) {
                            notify(
                                Random(System.currentTimeMillis()).nextInt(1000),
                                notification
                            )
                        }
                    }
                }
            } else {
                WorkManager.getInstance(applicationContext).cancelWorkById(id)
            }*/
            return Result.success()
        }
    }


}
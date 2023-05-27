package nl.tiebe.otarium.utils

import kotlinx.coroutines.runBlocking
import nl.tiebe.otarium.magister.refreshGrades
import platform.BackgroundTasks.BGAppRefreshTaskRequest
import platform.BackgroundTasks.BGTaskScheduler
import platform.Foundation.NSDate
import platform.Foundation.NSUUID
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotification
import platform.UserNotifications.UNNotificationPresentationOptionAlert
import platform.UserNotifications.UNNotificationPresentationOptions
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationResponse
import platform.UserNotifications.UNUserNotificationCenter
import platform.UserNotifications.UNUserNotificationCenterDelegateProtocol
import platform.darwin.NSObject

actual fun reloadTokensBackground(delay: Long) {
    val request = BGAppRefreshTaskRequest(identifier = "nl.tiebe.otarium.tokenrefresh")
    request.earliestBeginDate = NSDate(timeIntervalSinceReferenceDate = delay.toDouble())

    try {
        BGTaskScheduler.sharedScheduler.submitTaskRequest(request, null)
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

actual fun refreshGradesBackground(delay: Long) {
    val request = BGAppRefreshTaskRequest(identifier = "nl.tiebe.otarium.graderefresh")
    request.earliestBeginDate = NSDate(timeIntervalSinceReferenceDate = delay.toDouble())

    try {
        BGTaskScheduler.sharedScheduler.submitTaskRequest(request, null)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun registerBackgroundTasks() {
    BGTaskScheduler.sharedScheduler.registerForTaskWithIdentifier(
        identifier = "nl.tiebe.otarium.graderefresh",
        usingQueue = null,
        launchHandler = { task ->
            sendNotification("Grades refreshed", "Your grades have been refreshed")

            refreshGradesBackground(15*60)
            runBlocking {
                try {
                    nl.tiebe.otarium.Data.selectedAccount.refreshGrades()
                    task?.setTaskCompletedWithSuccess(true)
                } catch (e: Exception) {
                    e.printStackTrace()
                    task?.setTaskCompletedWithSuccess(false)
                }
            }
        }
    )

    BGTaskScheduler.sharedScheduler.registerForTaskWithIdentifier(
        identifier = "nl.tiebe.otarium.tokenrefresh",
        usingQueue = null,
        launchHandler = { task ->
            reloadTokensBackground(15*60)

            runBlocking {
                try {
                    nl.tiebe.otarium.Data.accounts.forEach {
                        it.refreshTokens()
                    }
                    task?.setTaskCompletedWithSuccess(true)
                } catch (e: Exception) {
                    e.printStackTrace()
                    task?.setTaskCompletedWithSuccess(false)
                }
            }
        }
    )
}

actual fun sendNotification(title: String, message: String) {
    val content = UNMutableNotificationContent()
    content.setTitle(title)
    content.setBody(message)

    val uuid = NSUUID.UUID().UUIDString()
    val request = UNNotificationRequest.requestWithIdentifier(uuid, content, null)

    val center = UNUserNotificationCenter.currentNotificationCenter()
    center.delegate = object : NSObject(), UNUserNotificationCenterDelegateProtocol {
        override fun userNotificationCenter(
            center: UNUserNotificationCenter,
            didReceiveNotificationResponse: UNNotificationResponse,
            withCompletionHandler: () -> Unit
        ) {
            withCompletionHandler()
        }

        override fun userNotificationCenter(
            center: UNUserNotificationCenter,
            willPresentNotification: UNNotification,
            withCompletionHandler: (UNNotificationPresentationOptions) -> Unit
        ) {
            withCompletionHandler(UNNotificationPresentationOptionAlert)
        }
    }

    center.addNotificationRequest(request) { error ->
        if (error != null) {
            println("Error: $error")
        } else {
            println("Notification sent")
        }
    }

}
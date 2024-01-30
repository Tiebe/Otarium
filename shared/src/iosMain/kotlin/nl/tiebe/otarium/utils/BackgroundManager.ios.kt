package nl.tiebe.otarium.utils

import kotlinx.coroutines.runBlocking
import nl.tiebe.otarium.magister.refreshGrades
import platform.BackgroundTasks.BGAppRefreshTaskRequest
import platform.BackgroundTasks.BGTaskScheduler
import platform.Foundation.NSDate
import platform.Foundation.NSUUID
import platform.UserNotifications.*
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

actual fun refreshMessagesBackground(delay: Long) {
}

fun registerBackgroundTasks() {
    BGTaskScheduler.sharedScheduler.registerForTaskWithIdentifier(
        identifier = "nl.tiebe.otarium.graderefresh",
        usingQueue = null,
        launchHandler = { task ->
            sendDebugNotification("Refreshing grades", "Refreshing your grades")

            refreshGradesBackground(15*60)
            runBlocking {
                try {
                    nl.tiebe.otarium.Data.selectedAccount.refreshGrades()
                    sendDebugNotification("Grades refreshed", "Your grades have been refreshed")
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
            sendDebugNotification("Refreshing tokens", "Refreshing your tokens")
            reloadTokensBackground(15*60)

            runBlocking {
                try {
                    nl.tiebe.otarium.Data.accounts.forEach {
                        it.refreshTokens()
                    }
                    sendDebugNotification("Tokens refreshed", "Your tokens have been refreshed")
                    task?.setTaskCompletedWithSuccess(true)
                } catch (e: Exception) {
                    e.printStackTrace()
                    task?.setTaskCompletedWithSuccess(false)
                }
            }
        }
    )
}

fun sendDebugNotification(title: String, message: String) {
    if (nl.tiebe.otarium.Data.debugNotifications) {
        sendNotification(title, message)
    }
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
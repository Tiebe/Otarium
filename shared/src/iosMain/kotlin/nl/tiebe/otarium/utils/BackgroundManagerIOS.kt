package nl.tiebe.otarium.utils

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

actual fun reloadTokensBackground() {

}

actual fun refreshGradesBackground() {

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
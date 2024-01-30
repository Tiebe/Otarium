package nl.tiebe.otarium

import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNUserNotificationCenter

actual fun setupNotifications() {
    val center = UNUserNotificationCenter.currentNotificationCenter()

    center.requestAuthorizationWithOptions(
        options = UNAuthorizationOptionAlert,
        completionHandler = { granted, _ ->
            if (granted) {
                println("Notifications granted")
            } else {
                println("Notifications denied")
            }
        }
    )
}

actual fun closeApp() {

}
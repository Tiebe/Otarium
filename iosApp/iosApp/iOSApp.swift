import SwiftUI
import shared
import UserNotifications

@main
struct iOSApp: App {
    var main: Main = Main()
    
    init() {
        main.setup()
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) { (success, error) in
            if let error = error {
                print("Request Notifications failed: \(error), \(error.localizedDescription)")
            }
        }
    }
    
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}

import SwiftUI
import shared
import UserNotifications
import OSLog

@main
struct iOSApp: App {
    var main: Main = Main()
    @State var finished: Bool = false

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
            if (finished) { Navigation() }
            if (Tokens().getPastTokens().isEmpty) {
                LoginView(finished: $finished)
            } else {
                Navigation()
            }
		}
	}
}

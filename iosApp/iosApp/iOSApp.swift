import SwiftUI
import shared
import FirebaseCore

@UIApplicationMain
class AppDelegate: NSObject, UIApplicationDelegate {
    var window: UIWindow?
    
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil
    ) -> Bool {
        let controller = AvoidDispose(RootViewControllersKt.RootViewController())
        controller.view.backgroundColor = .white
        
        IOS.shared.viewController = controller
        
        let window = UIWindow(frame: UIScreen.main.bounds)
        window.backgroundColor = .white
        window.rootViewController = controller
        window.makeKeyAndVisible()
        self.window = window
        
        FirebaseApp.configure()
        BackgroundManagerIOSKt.registerBackgroundTasks()
        
        return true
    }
}

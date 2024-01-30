import SwiftUI
import shared

@UIApplicationMain
class AppDelegate: NSObject, UIApplicationDelegate {
    var window: UIWindow?
    
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil
    ) -> Bool {
        let controller = AvoidDispose(RootViewControllersKt.RootViewController())
        controller.view.backgroundColor = .clear
        controller.view.isOpaque = false
        
        IOS.shared.viewController = controller
        
        let window = UIWindow(frame: UIScreen.main.bounds)
        window.backgroundColor = .clear
        window.isOpaque = false
        window.rootViewController = controller
        window.makeKeyAndVisible()
        self.window = window
        
        BackgroundManager_iosKt.registerBackgroundTasks()
        
        return true
    }
}

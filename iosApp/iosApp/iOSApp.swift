import SwiftUI
import test

@UIApplicationMain
class AppDelegate: NSObject, UIApplicationDelegate {
    var window: UIWindow?
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        let controller = AvoidDispose(RootViewControllersKt.RootViewController())
        controller.view.backgroundColor = .white 
        let window = UIWindow(frame: UIScreen.main.bounds)
        IOS.shared.window = window
        window.backgroundColor = .white
        window.rootViewController = controller
        window.makeKeyAndVisible()
        self.window = window
        
        
        BackgroundManagerIOSKt.registerBackgroundTasks()
        
        return true
    }
}

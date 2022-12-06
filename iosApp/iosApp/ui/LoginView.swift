import SwiftUI
import OSLog
import WebKit
import shared

struct WebView: UIViewRepresentable {
    var closeFunction : (() -> Void)?
   
    let loginUrl = LoginKt.getUrl()
    
    class Coordinator : NSObject, WKNavigationDelegate {
        var closeFunction : (() -> Void)?
        var loginUrl: KotlinPair<NSString, NSString>?
        
        func webView(_ webView: WKWebView, decidePolicyFor navigationAction: WKNavigationAction) async -> WKNavigationActionPolicy {
            Logger().info("\(navigationAction.request.url!.absoluteString)")
            
            
            if (navigationAction.request.url!.absoluteString.starts(with: "m6loapp://oauth2redirect")) {
                let code = getQueryStringParameter(
                    url: navigationAction.request.url!.absoluteString.replacingOccurrences(of: "#", with: "?"),
                    param: "code")
                
                do {
                    if (code != nil && loginUrl?.second != nil) {
                        //try await LoginKt.exchangeUrl(loginRequest: LoginRequest(code: code! as String, codeVerifier: loginUrl.second! as String))
                        
                        // if successful get firebase token but we need to pay for that
                        
                        closeFunction?()
                    }
                } catch let error {
                    Logger().log("\(error.localizedDescription)")
                }
            }
            Logger().info("\(navigationAction.request.url!.absoluteString.starts(with: "https://www.magister.nl"))")
            if (navigationAction.request.url!.absoluteString.starts(with: "https://www.magister.nl")) { // Gebruiksvoorwaarden bypass
                closeFunction?()
            }
            
            return WKNavigationActionPolicy.allow
        }
        
        func getQueryStringParameter(url: String, param: String) -> String? {
            guard let url = URLComponents(string: url) else { return nil }
            return url.queryItems?.first(where: { $0.name == param })?.value
        }
    }
    
    func makeCoordinator() -> Coordinator {
        return Coordinator()
    }
    
    func makeUIView(context: Context) -> WKWebView {
        let webConfig = WKWebViewConfiguration()
        let webView = WKWebView(frame: .zero, configuration: webConfig)
        
        return webView
    }

    func updateUIView(_ webView: WKWebView, context: Context) {
        webView.navigationDelegate = context.coordinator
        context.coordinator.closeFunction = closeFunction
        context.coordinator.loginUrl = loginUrl

        let myURL = URL(string: loginUrl.first! as String)
        let myRequest = URLRequest(url: myURL!)
        webView.load(myRequest)
        
    }
}


struct LoginView: View {
    @Binding var finished: Bool
    
    var body: some View {
        WebView(closeFunction: {
            Logger().info("test1")
            finished = false
        })
    }
}

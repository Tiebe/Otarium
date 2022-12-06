import SwiftUI


struct Navigation: View {
    var body: some View {
        TabView {
            MainView()
                .tabItem {
                    Label("Main", systemImage: "list.dash")
                }
            
            
        }
    }
}

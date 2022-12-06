import SwiftUI
import shared

struct ContentView: View {
    let greet = String(MainKt.settings.getInt(key: "version", defaultValue: 0))
	var body: some View {
		Text(greet)
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}

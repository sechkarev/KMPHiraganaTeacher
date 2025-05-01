import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    
    init() {
        AppModuleKt.doInitKoin(
            textRecognizer2: IosTestRecognizer()
        )
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

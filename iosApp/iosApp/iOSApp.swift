import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    
    init() {
        AppModuleKt.doInitKoin(
            textRecognizer2: IosTestRecognizer(),
            textToSpeechEngine: IosTextToSpeech(),
        )
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

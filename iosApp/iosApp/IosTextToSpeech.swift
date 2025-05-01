import ComposeApp
import AVFAudio
import os

class IosTextToSpeech: TextToSpeechEngine {
    
    let synthesizer = AVSpeechSynthesizer()
    let voice = AVSpeechSynthesisVoice(language: "ja")
    
    func initialise(onCompletion: @escaping () -> Void) {
        // does nothing
    }
    
    func pronounce(text: String) {
        os_log("pronouncing \(text)")
        let utterance = AVSpeechUtterance(string: text)
        utterance.voice = voice
        synthesizer.speak(utterance)
    }
}

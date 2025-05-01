//
//  TextRecognizer2.swift
//  iosApp
//
//  Created by Vladimir Sechkarev on 30.04.25.
//  Copyright © 2025 orgName. All rights reserved.
//

import ComposeApp
import MLKitDigitalInkRecognition

final class IosTestRecognizer: TextRecognizer2 {
    private var recognizer: DigitalInkRecognizer
    private var model: DigitalInkRecognitionModel
    private var modelManager: ModelManager
    private var strokes: [MLKitDigitalInkRecognition.Stroke] = []
    private var points: [StrokePoint] = []
    
    init() {
        modelManager = ModelManager.modelManager()
        let identifier = DigitalInkRecognitionModelIdentifier(forLanguageTag: "ja")
        model = DigitalInkRecognitionModel.init(modelIdentifier: identifier!)
        let options: DigitalInkRecognizerOptions = DigitalInkRecognizerOptions.init(model: model)
        recognizer = DigitalInkRecognizer.digitalInkRecognizer(options: options)
    }
    
    func initialize(onSuccess: @escaping () -> Void, onFailure: @escaping () -> Void) {
        let conditions = ModelDownloadConditions.init(allowsCellularAccess: true, allowsBackgroundDownloading: true)
        // To know when this method is done, observe the .mlkitModelDownloadDidSucceed and .mlkitModelDownloadDidFail notifications defined in MLKModelDownloadNotifications.h. If the latest model is already downloaded, completes without additional work and posts .mlkitModelDownloadDidSucceed notification, indicating that the model is ready to use.
        modelManager.download(model, conditions: conditions)
        NotificationCenter.default.addObserver(
            forName: NSNotification.Name.mlkitModelDownloadDidSucceed,
            object: nil,
            queue: OperationQueue.main,
            using: {
                [unowned self]
                (notification) in
                if notification.userInfo![ModelDownloadUserInfoKey.remoteModel.rawValue]
                    as? DigitalInkRecognitionModel == self.model
                {
                    onSuccess()
                }
            }
        )
        NotificationCenter.default.addObserver(
            forName: NSNotification.Name.mlkitModelDownloadDidFail,
            object: nil,
            queue: OperationQueue.main,
            using: {
                [unowned self]
                (notification) in
                if notification.userInfo![ModelDownloadUserInfoKey.remoteModel.rawValue]
                    as? DigitalInkRecognitionModel == self.model
                {
                    onFailure()
                }
            }
        )
    }
    
    func recognizeCurrentText() async throws -> String {
        let ink = Ink.init(strokes: strokes)
        return try await recognizer.recognize(ink: ink).candidates.first!.text
    }
    
    func cleanCurrentData() {
        strokes = []
        points = []
    }
    
    func startNewStroke() {
        
    }
    
    func completeStroke() {
        strokes.append(MLKitDigitalInkRecognition.Stroke(points: points))
        points = []
    }
    
    func currentStrokeAmount() -> Int32 {
        return Int32(strokes.count)
    }
    
    func addNewPoint(point: Point) {
        points.append(StrokePoint(x: point.x, y: point.y))
    }
}

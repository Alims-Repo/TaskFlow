//
//  KotlinFlow.swift
//  iosApp
//
//  Created by Abdul Alim Monshi on 11/19/25.
//
import Shared

class FlowCollector<T>: Kotlinx_coroutines_coreFlowCollector {
    let callback: (T) -> Void

    init(callback: @escaping (T) -> Void) {
        self.callback = callback
    }

    func emit(value: Any?, completionHandler: @escaping (Error?) -> Void) {
        if let typedValue = value as? T {
            callback(typedValue)
        }
        completionHandler(nil)
    }
}
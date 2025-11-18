//
// Created by Abdul Alim Monshi on 11/19/25.
//

import SwiftUI
import Shared

struct ModernTextFieldStyle: TextFieldStyle {
    func _body(configuration: TextField<Self._Label>) -> some View {
        configuration
            .padding()
            .background(Color(.tertiarySystemBackground))
            .cornerRadius(10)
    }
}

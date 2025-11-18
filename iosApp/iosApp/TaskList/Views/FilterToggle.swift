//
// Created by Abdul Alim Monshi on 11/19/25.
//

import SwiftUI
import Shared

struct FilterToggle: View {
    let title: String
    let icon: String
    let color: Color
    @Binding var isOn: Bool

    var body: some View {
        Toggle(isOn: $isOn) {
            HStack(spacing: 12) {
                Image(systemName: icon)
                    .foregroundColor(color)
                    .frame(width: 24)

                Text(title)
                    .font(.subheadline.weight(.medium))
            }
        }
    }
}
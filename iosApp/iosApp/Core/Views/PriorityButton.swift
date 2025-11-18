//
//  PriorityButton.swift
//  iosApp
//
//  Created by Abdul Alim Monshi on 11/19/25.
//
import SwiftUI
import Shared

struct PriorityButton: View {
    let priority: Priority
    let isSelected: Bool
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            VStack(spacing: 8) {
                Image(systemName: "flag.fill")
                    .font(.title2)
                    .foregroundColor(isSelected ? .white : priority.color)

                Text(priority.displayName)
                    .font(.caption.weight(.semibold))
                    .foregroundColor(isSelected ? .white : .primary)
            }
            .frame(maxWidth: .infinity)
            .padding(.vertical, 16)
            .background(
                RoundedRectangle(cornerRadius: 12)
                    .fill(isSelected ? priority.color : Color(.tertiarySystemBackground))
            )
        }
    }
}

// MARK: - Status Button
struct StatusButton: View {
    let status: TaskStatus
    let isSelected: Bool
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            HStack {
                Image(systemName: isSelected ? "checkmark.circle.fill" : "circle")
                    .foregroundColor(isSelected ? status.color : .gray)

                Text(status.displayName)
                    .font(.subheadline.weight(.medium))
                    .foregroundColor(.primary)

                Spacer()
            }
            .padding()
            .background(
                RoundedRectangle(cornerRadius: 10)
                    .fill(isSelected ? status.color.opacity(0.15) : Color(.tertiarySystemBackground))
            )
        }
    }
}

//
//  PriorityBadge.swift
//  iosApp
//
//  Created by Abdul Alim Monshi on 11/18/25.
//
import SwiftUI
import Shared

struct PriorityBadge: View {
    let priority: Priority

    var body: some View {
        Text(priority.displayName)
            .font(.caption)
            .padding(.horizontal, 8)
            .padding(.vertical, 4)
            .background(priority.color.opacity(0.2))
            .foregroundColor(priority.color)
            .cornerRadius(4)
    }
}

struct StatusBadge: View {
    let status: TaskStatus

    var body: some View {
        Text(status.displayName)
            .font(.caption)
            .padding(.horizontal, 8)
            .padding(.vertical, 4)
            .background(status.color.opacity(0.2))
            .foregroundColor(status.color)
            .cornerRadius(4)
    }
}

struct DueDateLabel: View {
    let dueDate: Kotlinx_datetimeInstant

    var body: some View {
        HStack(spacing: 4) {
            Image(systemName: "calendar")
            Text(dueDate.toDate().formatted(date: .abbreviated, time: .shortened))
        }
        .font(.caption)
        .foregroundColor(dueDate.isOverdue ? .red : .secondary)
    }
}


extension Priority {
    var displayName: String {
        switch self {
        case .low: return "Low"
        case .medium: return "Medium"
        case .high: return "High"
        default: return "Unknown"
        }
    }

    var color: Color {
        switch self {
        case .low: return .blue
        case .medium: return .orange
        case .high: return .red
        default: return .gray
        }
    }
}

extension TaskStatus {
    var displayName: String {
        switch self {
        case .todo: return "To Do"
        case .inProgress: return "In Progress"
        case .done: return "Completed"
        default: return "Unknown"
        }
    }

    var color: Color {
        switch self {
        case .todo: return .gray
        case .inProgress: return .orange
        case .done: return .green
        default: return .gray
        }
    }
}


extension Task: Identifiable {}


struct StatusSelector: View {
    let task: Task
    let update: (TaskStatus) -> Void

    var body: some View {
        HStack(spacing: 14) {
            statusButton(
                status: .todo,
                filled: "circle.fill",
                regular: "circle"
            )

            statusButton(
                status: .inProgress,
                filled: "clock.fill",
                regular: "clock"
            )

            statusButton(
                status: .done,
                filled: "checkmark.circle.fill",
                regular: "checkmark.circle"
            )
        }
        .padding(.vertical, 6)
    }

    @ViewBuilder
    func statusButton(
        status: TaskStatus,
        filled: String,
        regular: String
    ) -> some View {
        let isSelected = task.status == status

        Button {
            withAnimation(.spring(response: 0.25, dampingFraction: 0.7)) {
                update(status)
            }
        } label: {
            Image(systemName: isSelected ? filled : regular)
                .font(.system(size: isSelected ? 20 : 17, weight: .medium))
                .foregroundColor(isSelected ? statusColor(status) : .gray.opacity(0.4))
                .scaleEffect(isSelected ? 1.15 : 1.0)
                .animation(.spring(response: 0.25), value: isSelected)
        }
        .buttonStyle(.plain)
    }

    func statusColor(_ status: TaskStatus) -> Color {
        switch status {
        case .todo:        return .gray
        case .inProgress:  return .blue
        case .done:        return .green
        default:           return .gray
        }
    }
}

// MARK: - Modern Badges
struct ModernPriorityBadge: View {
    let priority: Priority

    var body: some View {
        HStack(spacing: 4) {
            Circle()
                .fill(priority.color)
                .frame(width: 6, height: 6)

            Text(priority.displayName)
                .font(.caption.weight(.medium))
                .foregroundColor(priority.color)
        }
        .padding(.horizontal, 10)
        .padding(.vertical, 5)
        .background(priority.color.opacity(0.15))
        .cornerRadius(8)
    }
}

struct ModernStatusBadge: View {
    let status: TaskStatus

    var body: some View {
        Circle()
            .fill(status.color.opacity(0.2))
            .frame(width: 8, height: 8)
            .overlay(
                Circle()
                    .stroke(status.color, lineWidth: 2)
            )
    }
}

struct ModernDueDateBadge: View {
    let dueDate: Kotlinx_datetimeInstant

    var isOverdue: Bool {
        dueDate.toDate() < Date()
    }

    var body: some View {
        HStack(spacing: 4) {
            Image(systemName: isOverdue ? "exclamationmark.circle.fill" : "calendar")
                .font(.caption)

            Text(dueDate.toDate().formatted(date: .abbreviated, time: .omitted))
                .font(.caption.weight(.medium))
        }
        .foregroundColor(isOverdue ? .red : .secondary)
        .padding(.horizontal, 10)
        .padding(.vertical, 5)
        .background(isOverdue ? Color.red.opacity(0.1) : Color.gray.opacity(0.1))
        .cornerRadius(8)
    }
}

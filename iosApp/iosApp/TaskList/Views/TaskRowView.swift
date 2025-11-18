//
//  TaskRowView.swift
//  iosApp
//
//  Created by Abdul Alim Monshi on 11/18/25.
//
import SwiftUI
import Shared

struct TaskRowView: View {
    let task: Task
    @ObservedObject var viewModel: TaskListViewModelWrapper

    var body: some View {
        HStack(spacing: 12) {
            // Status Checkbox
            Button {
                let newStatus: TaskStatus = task.status == .done ? .todo : .done
                viewModel.updateTaskStatus(id: task.id, status: newStatus)
            } label: {
                Image(systemName: task.status == .done ? "checkmark.circle.fill" : "circle")
                    .foregroundColor(task.status == .done ? .green : .gray)
                    .font(.title3)
            }
            .buttonStyle(.plain)

            VStack(alignment: .leading, spacing: 4) {
                Text(task.title)
                    .font(.headline)
                    .strikethrough(task.status == .done)

                if let description = task.description_, !description.isEmpty {
                    Text(description)
                        .font(.subheadline)
                        .foregroundColor(.secondary)
                        .lineLimit(1)
                }

                HStack {
                    PriorityBadge(priority: task.priority)

                    if let dueDate = task.dueDate {
                        DueDateLabel(dueDate: dueDate)
                    }
                }
            }

            Spacer()

            StatusBadge(status: task.status)
        }
        .padding(.vertical, 4)
    }
}

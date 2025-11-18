//
//  EditTaskView.swift
//  iosApp
//
//  Created by Abdul Alim Monshi on 11/19/25.
//
import SwiftUI
import Shared

struct EditTaskView: View {
    @Environment(\.dismiss) var dismiss
    let task: Task
    @ObservedObject var viewModel: TaskListViewModelWrapper

    @State private var title: String
    @State private var description: String
    @State private var priority: Priority
    @State private var status: TaskStatus
    @State private var dueDate: Date?
    @State private var hasDueDate: Bool

    init(task: Task, viewModel: TaskListViewModelWrapper) {
        self.task = task
        self.viewModel = viewModel
        _title = State(initialValue: task.title)
        _description = State(initialValue: task.description_ ?? "")
        _priority = State(initialValue: task.priority)
        _status = State(initialValue: task.status)
        _dueDate = State(initialValue: task.dueDate?.toDate())
        _hasDueDate = State(initialValue: task.dueDate != nil)
    }

    var body: some View {
        NavigationView {
            ZStack {
                LinearGradient(
                    colors: [Color(.systemBackground), Color.blue.opacity(0.03)],
                    startPoint: .top,
                    endPoint: .bottom
                )
                    .ignoresSafeArea()

                ScrollView {
                    VStack(spacing: 24) {
                        // Same layout as AddTaskView
                        VStack(spacing: 16) {
                            VStack(alignment: .leading, spacing: 8) {
                                Label("Title", systemImage: "text.alignleft")
                                    .font(.subheadline.weight(.semibold))
                                    .foregroundColor(.secondary)

                                TextField("Enter task title", text: $title)
                                    .textFieldStyle(ModernTextFieldStyle())
                            }

                            VStack(alignment: .leading, spacing: 8) {
                                Label("Description", systemImage: "doc.text")
                                    .font(.subheadline.weight(.semibold))
                                    .foregroundColor(.secondary)

                                TextField("Add details (optional)", text: $description, axis: .vertical)
                                    .textFieldStyle(ModernTextFieldStyle())
                                    .lineLimit(3...6)
                            }
                        }
                        .padding()
                        .background(
                            RoundedRectangle(cornerRadius: 16)
                                .fill(Color(.secondarySystemBackground))
                                .shadow(color: .black.opacity(0.05), radius: 10)
                        )

                        VStack(alignment: .leading, spacing: 12) {
                            Label("Priority", systemImage: "flag.fill")
                                .font(.subheadline.weight(.semibold))
                                .foregroundColor(.secondary)

                            HStack(spacing: 12) {
                                ForEach([Priority.low, Priority.medium, Priority.high], id: \.self) { p in
                                    PriorityButton(priority: p, isSelected: priority == p) {
                                        withAnimation(.spring(response: 0.3)) {
                                            priority = p
                                        }
                                    }
                                }
                            }
                        }
                        .padding()
                        .background(
                            RoundedRectangle(cornerRadius: 16)
                                .fill(Color(.secondarySystemBackground))
                                .shadow(color: .black.opacity(0.05), radius: 10)
                        )

                        VStack(alignment: .leading, spacing: 12) {
                            Label("Status", systemImage: "circle.hexagongrid.fill")
                                .font(.subheadline.weight(.semibold))
                                .foregroundColor(.secondary)

                            VStack(spacing: 8) {
                                ForEach([TaskStatus.todo, TaskStatus.inProgress, TaskStatus.done], id: \.self) { s in
                                    StatusButton(status: s, isSelected: status == s) {
                                        withAnimation(.spring(response: 0.3)) {
                                            status = s
                                        }
                                    }
                                }
                            }
                        }
                        .padding()
                        .background(
                            RoundedRectangle(cornerRadius: 16)
                                .fill(Color(.secondarySystemBackground))
                                .shadow(color: .black.opacity(0.05), radius: 10)
                        )

                        VStack(alignment: .leading, spacing: 12) {
                            Toggle(isOn: $hasDueDate) {
                                Label("Set Due Date", systemImage: "calendar.badge.clock")
                                    .font(.subheadline.weight(.semibold))
                            }
                            .tint(.blue)

                            if hasDueDate {
                                DatePicker(
                                    "Due Date",
                                    selection: Binding(
                                        get: { dueDate ?? Date() },
                                        set: { dueDate = $0 }
                                    ),
                                    displayedComponents: [.date, .hourAndMinute]
                                )
                                    .datePickerStyle(.graphical)
                                    .tint(.blue)
                            }
                        }
                        .padding()
                        .background(
                            RoundedRectangle(cornerRadius: 16)
                                .fill(Color(.secondarySystemBackground))
                                .shadow(color: .black.opacity(0.05), radius: 10)
                        )
                    }
                    .padding()
                }
            }
            .navigationTitle("Edit Task")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Cancel") {
                        dismiss()
                    }
                    .foregroundColor(.red)
                }

                ToolbarItem(placement: .confirmationAction) {
                    Button {
                        saveTask()
                    } label: {
                        Text("Save")
                            .fontWeight(.semibold)
                    }
                    .disabled(title.isEmpty)
                }
            }
        }
    }

    private func saveTask() {
        let instant = hasDueDate ? dueDate?.toKotlinInstant() : nil
        let desc = description.isEmpty ? nil : description

        viewModel.updateTask(
            id: task.id,
            title: title,
            description: desc,
            priority: priority,
            status: status,
            dueDate: instant
        )

        dismiss()
    }
}

extension Kotlinx_datetimeInstant {
    var isOverdue: Bool {
        self.toDate() < Date()
    }
}

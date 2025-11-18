//
//  TaskDetailView.swift
//  iosApp
//
//  Created by Abdul Alim Monshi on 11/19/25.
//
import SwiftUI
import Shared

struct TaskDetailView: View {
    let task: Task
    @ObservedObject var viewModel: TaskListViewModelWrapper
    @State private var showEditSheet = false
    @State private var showDeleteAlert = false
    @Environment(\.dismiss) var dismiss

    var body: some View {
        ScrollView {
            VStack(spacing: 24) {
                // Header Card
                VStack(alignment: .leading, spacing: 16) {
                    HStack(alignment: .top) {
                        VStack(alignment: .leading, spacing: 8) {
                            Text(task.title)
                                .font(.title2.bold())
                                .foregroundColor(.primary)

                            HStack(spacing: 8) {
                                ModernStatusBadge(status: task.status)
                                    .scaleEffect(1.5)
                                    .padding(.trailing, 4)

                                ModernPriorityBadge(priority: task.priority)
                            }
                        }

                        Spacer()

                        Button {
                            withAnimation {
                                let newStatus: TaskStatus = task.status == .done ? .todo : .done
                                viewModel.updateTaskStatus(id: task.id, status: newStatus)
                            }
                        } label: {
                            Image(systemName: task.status == .done ? "checkmark.circle.fill" : "circle")
                                .font(.system(size: 32))
                                .foregroundColor(task.status == .done ? .green : .gray)
                        }
                    }
                }
                .padding()
                .background(
                    RoundedRectangle(cornerRadius: 16)
                        .fill(Color(.secondarySystemBackground))
                        .shadow(color: .black.opacity(0.05), radius: 10)
                )

                // Description Card
                if let description = task.description_, !description.isEmpty {
                    VStack(alignment: .leading, spacing: 12) {
                        Label("Description", systemImage: "doc.text")
                            .font(.headline)
                            .foregroundColor(.primary)

                        Text(description)
                            .font(.body)
                            .foregroundColor(.secondary)
                            .lineSpacing(4)
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding()
                    .background(
                        RoundedRectangle(cornerRadius: 16)
                            .fill(Color(.secondarySystemBackground))
                            .shadow(color: .black.opacity(0.05), radius: 10)
                    )
                }

                // Details Card
                VStack(spacing: 16) {
                    DetailInfoRow(
                        icon: "calendar.badge.plus",
                        label: "Created",
                        value: task.createdAt.toDate().formatted(date: .long, time: .shortened),
                        color: .blue
                    )

                    Divider()

                    if let dueDate = task.dueDate {
                        DetailInfoRow(
                            icon: "calendar.badge.clock",
                            label: "Due Date",
                            value: dueDate.toDate().formatted(date: .long, time: .shortened),
                            color: dueDate.isOverdue ? .red : .orange
                        )
                    } else {
                        DetailInfoRow(
                            icon: "calendar.badge.clock",
                            label: "Due Date",
                            value: "Not set",
                            color: .gray
                        )
                    }
                }
                .padding()
                .background(
                    RoundedRectangle(cornerRadius: 16)
                        .fill(Color(.secondarySystemBackground))
                        .shadow(color: .black.opacity(0.05), radius: 10)
                )

                // Actions
                VStack(spacing: 12) {
                    Button {
                        showEditSheet = true
                    } label: {
                        Label("Edit Task", systemImage: "pencil")
                            .font(.headline)
                            .foregroundColor(.white)
                            .frame(maxWidth: .infinity)
                            .padding()
                            .background(
                                LinearGradient(
                                    colors: [.blue, .purple],
                                    startPoint: .leading,
                                    endPoint: .trailing
                                )
                            )
                            .cornerRadius(12)
                    }

                    Button {
                        showDeleteAlert = true
                    } label: {
                        Label("Delete Task", systemImage: "trash")
                            .font(.headline)
                            .foregroundColor(.white)
                            .frame(maxWidth: .infinity)
                            .padding()
                            .background(Color.red)
                            .cornerRadius(12)
                    }
                }
                .padding(.top, 8)
            }
            .padding()
        }
        .background(
            LinearGradient(
                colors: [Color(.systemBackground), Color.blue.opacity(0.03)],
                startPoint: .top,
                endPoint: .bottom
            )
                .ignoresSafeArea()
        )
        .navigationBarTitleDisplayMode(.inline)
        .sheet(isPresented: $showEditSheet) {
            EditTaskView(task: task, viewModel: viewModel)
        }
        .alert("Delete Task", isPresented: $showDeleteAlert) {
            Button("Cancel", role: .cancel) {}
            Button("Delete", role: .destructive) {
                viewModel.deleteTask(id: task.id)
                dismiss()
            }
        } message: {
            Text("Are you sure you want to delete this task? This action cannot be undone.")
        }
    }
}

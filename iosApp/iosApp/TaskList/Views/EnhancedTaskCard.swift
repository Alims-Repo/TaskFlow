//
//  EnhancedTaskCard.swift
//  iosApp
//
//  Created by Abdul Alim Monshi on 11/18/25.
//
import SwiftUI
import Shared

struct EnhancedTaskCard: View {
    let task: Task
    @ObservedObject var viewModel: TaskListViewModelWrapper
    @State private var isPressed = false
    @State private var showEditSheet = false

    var body: some View {
        VStack(alignment: .leading, spacing: 6) {
            HStack() {
                Text(task.title)
                    .font(.headline)
                    .foregroundColor(.primary)
                    .strikethrough(task.status == .done, color: .gray)
                
                Spacer()
                
                ModernPriorityBadge(priority: task.priority)
            }
            
            if let description = task.description_, !description.isEmpty {
                Text(description)
                    .font(.subheadline)
                    .foregroundColor(.secondary)
                    .lineLimit(2)
            }
            
            HStack(spacing: 8) {
                if let dueDate = task.dueDate {
                    ModernDueDateBadge(dueDate: dueDate)
                }
                
                Spacer()
                
                HStack(spacing: 14) {
                    StatusSelector(task: task) { newStatus in
                        viewModel.updateTaskStatus(id: task.id, status: newStatus)
                    }
                }
                .padding(.vertical, 2)
                .padding(.horizontal, 8)
                .background(
                    RoundedRectangle(cornerRadius: 12)
                        .fill(Color(.systemGray6))
                )
                .onTapGesture {}
            }
        }
        .sheet(isPresented: $showEditSheet) {
            EditTaskView(task: task, viewModel: viewModel)
        }
    }
}

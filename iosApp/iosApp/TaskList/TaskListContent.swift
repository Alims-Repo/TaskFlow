//
//  TaskListContent.swift
//  iosApp
//
//  Created by Abdul Alim Monshi on 11/18/25.
//
import SwiftUI
import Shared

struct TaskListContent: View {
    @ObservedObject var viewModel: TaskListViewModelWrapper
    @State private var tasks: [Task] = []
    
    @State private var taskToDelete: Task?
    @State private var taskToEdit: Task?
    
    @State private var selectedTask: Task? = nil

    var body: some View {
        List {
            
            if let stats = viewModel.statistics {
                Section {
                    StatisticsCard(statistics: stats)
                }
            }
            
            Section("Tasks") {
                ForEach(tasks, id: \.id) { task in
                    
                    ZStack {
                        NavigationLink(destination: TaskDetailView(task: task, viewModel: viewModel)) {
                            EmptyView()
                        }
                        .opacity(0)
                        EnhancedTaskCard(task: task, viewModel: viewModel)
                            .contentShape(Rectangle())
                    }
                    .swipeActions {
                        Button {
                            taskToDelete = task
                        } label: {
                            Label("Delete", systemImage: "trash")
                        }.tint(.red)
                        
                        Button {
                            taskToEdit = task
                        } label: {
                            Label("Edit", systemImage: "pencil")
                        }
                        .tint(.blue)
                    }
                }
            }
        }
        .animation(.default, value: tasks)
        .onReceive(viewModel.$uiState) { state in
            if case let .success(tasks) = state {
                self.tasks = tasks
            }
        }
        .alert("Delete Task", isPresented: Binding(
            get: { taskToDelete != nil },
            set: { if !$0 { taskToDelete = nil } }
        )) {
            Button("Cancel", role: .cancel) {}
            Button("Delete", role: .destructive) {
                if let task = taskToDelete {
                    withAnimation {
                        viewModel.deleteTask(id: task.id)
                    }
                    taskToDelete = nil
                }
            }
        } message: {
            if let task = taskToDelete {
                Text("Are you sure you want to delete \"\(task.title)\"? This action cannot be undone.")
            } else {
                Text("")
            }
        }
        .sheet(item: $taskToEdit) { task in
            EditTaskView(task: task, viewModel: viewModel)
        }
        .navigationDestination(item: $selectedTask) { task in
            TaskDetailView(task: task, viewModel: viewModel)
        }
    }
}

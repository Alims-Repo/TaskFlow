//
// Created by Abdul Alim Monshi on 11/17/25.
//

import SwiftUI
import Shared
import Combine

struct FilterView: View {
    @Environment(\.dismiss) var dismiss
    @ObservedObject var viewModel: TaskListViewModelWrapper

    @State private var selectedStatuses: Set<TaskStatus> = []
    @State private var selectedPriorities: Set<Priority> = []
    @State private var showOverdueOnly = false
    @State private var sortOption: SortOption = .dateCreatedDesc

    var body: some View {
        NavigationView {
            ZStack {
                LinearGradient(
                    colors: [Color(.systemBackground), Color.blue.opacity(0.03)],
                    startPoint: .top,
                    endPoint: .bottom
                )
                    .ignoresSafeArea()

                List {
                    Section {
                        Picker("Sort", selection: $sortOption) {
                            ForEach(SortOption.allCases, id: \.self) { option in
                                HStack {
                                    Image(systemName: option.icon)
                                    Text(option.displayName)
                                }
                                .tag(option)
                            }
                        }
                        .pickerStyle(.menu)
                    } header: {
                        Label("Sort By", systemImage: "arrow.up.arrow.down")
                            .font(.headline)
                            .foregroundColor(.secondary)
                    }
                    

                    Section {
                        ForEach([TaskStatus.todo, TaskStatus.inProgress, TaskStatus.done], id: \.self) { status in
                            FilterToggle(
                                title: status.displayName,
                                icon: "circle.fill",
                                color: status.color,
                                isOn: Binding(
                                    get: { selectedStatuses.contains(status) },
                                    set: { isOn in
                                        if isOn {
                                            selectedStatuses.insert(status)
                                        } else {
                                            selectedStatuses.remove(status)
                                        }
                                    }
                                )
                            )
                        }
                    } header: {
                        Label("Filter by Priority", systemImage: "flag")
                            .font(.headline)
                            .foregroundColor(.secondary)
                    }
                   

                    Section {
                        ForEach([Priority.low, Priority.medium, Priority.high], id: \.self) { priority in
                            FilterToggle(
                                title: priority.displayName,
                                icon: "flag.fill",
                                color: priority.color,
                                isOn: Binding(
                                    get: { selectedPriorities.contains(priority) },
                                    set: { isOn in
                                        if isOn {
                                            selectedPriorities.insert(priority)
                                        } else {
                                            selectedPriorities.remove(priority)
                                        }
                                    }
                                )
                            )
                        }
                    } header: {
                        Label("Other Filters", systemImage: "slider.horizontal.3")
                            .font(.headline)
                            .foregroundColor(.secondary)
                    }
                    

                    // Overdue Toggle
                    Section {
                        FilterToggle(
                            title: "Show Overdue Only",
                            icon: "exclamationmark.triangle.fill",
                            color: .red,
                            isOn: $showOverdueOnly
                        )
                    }
                    // Clear Button
                    Button {
                        withAnimation {
                            selectedStatuses.removeAll()
                            selectedPriorities.removeAll()
                            showOverdueOnly = false
                            sortOption = .dateCreatedDesc
                            viewModel.clearFilters()
                        }
                    } label: {
                        Label("Clear All Filters", systemImage: "xmark.circle.fill")
                            .font(.headline)
                            .foregroundColor(.white)
                            .frame(maxWidth: .infinity)
                            .padding()
                            .background(
                                LinearGradient(
                                    colors: [.red, .orange],
                                    startPoint: .leading,
                                    endPoint: .trailing
                                )
                            )
                            .cornerRadius(12)
                    }
                }
            }
            .navigationTitle("Filters & Sort")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .confirmationAction) {
                    Button {
                        applyFilters()
                        dismiss()
                    } label: {
                        Text("Apply")
                            .fontWeight(.semibold)
                    }
                }
            }
        }
    }

    private func applyFilters() {
        viewModel.setSortOption(option: sortOption)
        viewModel.applyFilters(
            statuses: selectedStatuses,
            priorities: selectedPriorities,
            showOverdueOnly: showOverdueOnly
        )
    }
}
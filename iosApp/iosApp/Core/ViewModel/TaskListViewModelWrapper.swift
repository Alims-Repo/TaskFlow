//
//  TaskListViewModelWrapper.swift
//  iosApp
//
//  Created by Abdul Alim Monshi on 11/19/25.
//
import SwiftUI
import Shared

class TaskListViewModelWrapper: ObservableObject {
    private let viewModel: TaskListViewModel

    @Published var uiState: TaskListState = .loading
    @Published var statistics: TaskStatistics?

    private var stateFlowWatcher: Closeable?
    private var statsFlowWatcher: Closeable?

    init() {
        let koinHelper = KoinHelper()
        self.viewModel = koinHelper.createTaskListViewModel()

        observeState()
        observeStatistics()
    }

    private func observeState() {
        let collector = FlowCollector<TaskListUiState> { [weak self] state in
            guard let self = self else { return }
            DispatchQueue.main.async {
                self.uiState = self.mapUiState(state)
            }
        }

        viewModel.uiState.collect(collector: collector) { error in
            if let error = error {
                print("Error collecting UI state: \(error)")
            }
        }
    }

    private func observeStatistics() {
        let collector = FlowCollector<TaskStatistics> { [weak self] stats in
            guard let self = self else { return }
            DispatchQueue.main.async {
                self.statistics = stats
            }
        }

        viewModel.statistics.collect(collector: collector) { error in
            if let error = error {
                print("Error collecting statistics: \(error)")
            }
        }
    }

    private func mapUiState(_ state: TaskListUiState) -> TaskListState {
        if state is TaskListUiState.Loading {
            return .loading
        } else if let success = state as? TaskListUiState.Success {
            return .success(success.tasks)
        } else if let error = state as? TaskListUiState.Error {
            return .error(error.message)
        }
        return .loading
    }

    func refresh() {
        observeState()
        observeStatistics()
    }

    func search(query: String) {
        viewModel.searchTasks(query: query)
    }

    func applyFilters(statuses: Set<TaskStatus>, priorities: Set<Priority>, showOverdueOnly: Bool) {
        viewModel.applyFilters(statuses: statuses, priorities: priorities, showOverdueOnly: showOverdueOnly)
    }

    func setSortOption(option: SortOption) {
        viewModel.setSortOption(option: option)
    }

    func clearFilters() {
        viewModel.clearFilters()
    }

    func addTask(title: String, description: String?, priority: Priority, status: TaskStatus, dueDate: Kotlinx_datetimeInstant?) {
        viewModel.addTask(title: title, description: description, priority: priority, status: status, dueDate: dueDate)
    }

    func updateTask(id: String, title: String, description: String?, priority: Priority, status: TaskStatus, dueDate: Kotlinx_datetimeInstant?) {
        viewModel.updateTask(id: id, title: title, description: description, priority: priority, status: status, dueDate: dueDate)
    }

    func updateTaskStatus(id: String, status: TaskStatus) {
        viewModel.updateTaskStatus(id: id, status: status)
    }

    func deleteTask(id: String) {
        viewModel.deleteTask(id: id)
    }

    deinit {
        stateFlowWatcher?.close()
        statsFlowWatcher?.close()
        viewModel.onCleared()
    }
}
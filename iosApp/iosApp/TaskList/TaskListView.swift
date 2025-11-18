//
//  TaskListView.swift
//  iosApp
//
//  Created by Abdul Alim Monshi on 11/18/25.
//
import SwiftUI
import Shared

struct TaskListView: View {
    @ObservedObject var viewModel: TaskListViewModelWrapper
    @State private var showAddTask = false
    @State private var showFilters = false
    @State private var searchText = ""

    var body: some View {
        ZStack {
            LinearGradient(
                colors: [Color(.systemBackground), Color.blue.opacity(0.05)],
                startPoint: .top,
                endPoint: .bottom
            ).ignoresSafeArea()

            switch viewModel.uiState {
            case .loading:
                LoadingView()

            case .success(let tasks):
                if tasks.isEmpty && searchText.isEmpty {
                    EmptyStateView()
                } else if tasks.isEmpty {
                    NoResultsView()
                } else {
                    TaskListContent(
//                        tasks: tasks,
                        viewModel: viewModel
                    )
                }

            case .error(let message):
                ErrorView(message: message) {
                    viewModel.refresh()
                }
            }
        }
        .navigationTitle("Task Flow")
        .navigationBarTitleDisplayMode(.large)
        .toolbar {
            ToolbarItem(placement: .navigationBarTrailing) {
                Button {
                    showAddTask = true
                } label: {
                    HStack(spacing: 4) {
                        Image(systemName: "plus.circle.fill")
                        Text("New")
                            .font(.subheadline.weight(.semibold))
                    }
                    .foregroundStyle(
                        LinearGradient(
                            colors: [.blue, .purple],
                            startPoint: .leading,
                            endPoint: .trailing
                        )
                    )
                }
            }

            ToolbarItem(placement: .navigationBarLeading) {
                Button {
                    showFilters = true
                } label: {
                    Image(systemName: "slider.horizontal.3")
                        .foregroundColor(.blue)
                }
            }
        }
        .searchable(text: $searchText, prompt: "Search by title...")
        .onChange(of: searchText) { newValue in
            viewModel.search(query: newValue)
        }
        .sheet(isPresented: $showAddTask) {
            AddTaskView(viewModel: viewModel)
        }
        .sheet(isPresented: $showFilters) {
            FilterView(viewModel: viewModel)
        }
    }
}

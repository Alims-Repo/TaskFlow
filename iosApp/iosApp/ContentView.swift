import SwiftUI
import Shared

struct ContentView: View {
    @StateObject private var viewModel = TaskListViewModelWrapper()

    var body: some View {
        NavigationView {
            TaskListView(viewModel: viewModel)
        }
        .navigationViewStyle(.stack)
    }
}

//
//  EmptyStateView.swift
//  iosApp
//
//  Created by Abdul Alim Monshi on 11/18/25.
//
import SwiftUI
import Shared

struct EmptyStateView: View {
    @State private var isAnimating = false

    var body: some View {
        VStack(spacing: 24) {
            ZStack {
                Circle()
                    .fill(
                        LinearGradient(
                            colors: [.blue.opacity(0.2), .purple.opacity(0.2)],
                            startPoint: .topLeading,
                            endPoint: .bottomTrailing
                        )
                    )
                    .frame(width: 120, height: 120)
                    .scaleEffect(isAnimating ? 1.1 : 1.0)
                    .animation(.easeInOut(duration: 2).repeatForever(autoreverses: true), value: isAnimating)

                Image(systemName: "tray")
                    .font(.system(size: 50))
                    .foregroundStyle(
                        LinearGradient(
                            colors: [.blue, .purple],
                            startPoint: .topLeading,
                            endPoint: .bottomTrailing
                        )
                    )
            }

            VStack(spacing: 8) {
                Text("No Tasks Yet")
                    .font(.title2.bold())
                    .foregroundColor(.primary)

                Text("Start organizing your tasks\nby creating your first one")
                    .font(.subheadline)
                    .foregroundColor(.secondary)
                    .multilineTextAlignment(.center)
            }
        }
        .onAppear {
            isAnimating = true
        }
    }
}

struct NoResultsView: View {
    var body: some View {
        VStack(spacing: 16) {
            Image(systemName: "magnifyingglass")
                .font(.system(size: 50))
                .foregroundColor(.gray.opacity(0.5))

            Text("No Results Found")
                .font(.title3.bold())

            Text("Try adjusting your search")
                .font(.subheadline)
                .foregroundColor(.secondary)
        }
    }
}

extension Kotlinx_datetimeInstant {
    func toDate() -> Date {
        Date(timeIntervalSince1970: TimeInterval(self.epochSeconds))
    }
}

extension Date {
    func toKotlinInstant() -> Kotlinx_datetimeInstant {
        Kotlinx_datetimeInstant.Companion().fromEpochMilliseconds(epochMilliseconds: Int64(self.timeIntervalSince1970 * 1000))
    }
}

//
//  StatisticsCard.swift
//  iosApp
//
//  Created by Abdul Alim Monshi on 11/18/25.
//
import SwiftUI
import Shared

struct StatisticsCard: View {
    let statistics: TaskStatistics

    var completionProgress: Double {
        guard statistics.total > 0 else { return 0 }
        return Double(statistics.done) / Double(statistics.total)
    }

    var body: some View {
        VStack {
            // Progress Section
            HStack {
                // Progress Ring
                ZStack {
                    Circle()
                        .stroke(Color(.systemGray5), lineWidth: 8)
                        .frame(width: 80, height: 80)

                    Circle()
                        .trim(from: 0, to: completionProgress)
                        .stroke(
                            Color.blue,
                            style: StrokeStyle(lineWidth: 8, lineCap: .round)
                        )
                        .frame(width: 80, height: 80)
                        .rotationEffect(.degrees(-90))
                        .animation(.spring(response: 0.6, dampingFraction: 0.8), value: completionProgress)

                    VStack(spacing: 0) {
                        Text("\(Int(completionProgress * 100))%")
                            .font(.system(size: 20, weight: .semibold, design: .rounded))
                            .foregroundColor(.primary)
                    }
                }
                
                Spacer()

                // Progress Details
                VStack(alignment: .leading, spacing: 4) {
                    Text("Task Progress")
                        .font(.system(size: 15, weight: .medium))
                        .foregroundColor(.secondary)

                    HStack(alignment: .firstTextBaseline, spacing: 4) {
                        Text("\(statistics.done)")
                            .font(.system(size: 32, weight: .bold, design: .rounded))
                            .foregroundColor(.primary)
                        Text("/ \(statistics.total)")
                            .font(.system(size: 18, weight: .medium))
                            .foregroundColor(.secondary)
                        
                        Text("completed")
                            .font(.system(size: 13))
                            .foregroundColor(.secondary)
                    }
                }
            }.padding(.horizontal, 20)
                .padding(.top, 4)
            
            Rectangle()
                .fill(Color(.systemGray5))
                .frame(height: 1)

            // Stats Grid
            HStack {
                StatMiniCard(
                    icon: "circle",
                    title: "To Do",
                    value: "\(statistics.todo)",
                    color: .secondary
                )

                Rectangle()
                    .fill(Color(.systemGray5))
                    .frame(width: 1, height: 50)

                StatMiniCard(
                    icon: "clock.fill",
                    title: "In Progress",
                    value: "\(statistics.inProgress)",
                    color: .orange
                )

                Rectangle()
                    .fill(Color(.systemGray5))
                    .frame(width: 1, height: 50)

                StatMiniCard(
                    icon: "checkmark.circle.fill",
                    title: "Completed",
                    value: "\(statistics.done)",
                    color: .green
                )
            }
        }
    }
}

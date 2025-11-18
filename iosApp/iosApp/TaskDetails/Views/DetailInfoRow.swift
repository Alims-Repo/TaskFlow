//
//  DetailInfoRow.swift
//  iosApp
//
//  Created by Abdul Alim Monshi on 11/19/25.
//
import SwiftUI
import Shared

struct DetailInfoRow: View {
    let icon: String
    let label: String
    let value: String
    let color: Color

    var body: some View {
        HStack(spacing: 12) {
            Image(systemName: icon)
                .font(.title3)
                .foregroundColor(color)
                .frame(width: 30)

            VStack(alignment: .leading, spacing: 4) {
                Text(label)
                    .font(.caption)
                    .foregroundColor(.secondary)

                Text(value)
                    .font(.subheadline.weight(.medium))
                    .foregroundColor(.primary)
            }

            Spacer()
        }
    }
}
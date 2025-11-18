//
// Created by Abdul Alim Monshi on 11/19/25.
//

import Shared

extension SortOption: CaseIterable {
    public static var allCases: [SortOption] {
        [
            .dateCreatedDesc,
            .dateCreatedAsc,
            .dueDateAsc,
            .dueDateDesc,
            .priorityHighToLow,
            .priorityLowToHigh,
            .titleAToZ,
            .titleZToA
        ]
    }

    var displayName: String {
        switch self {
        case .dateCreatedDesc: return "Date Created (Newest First)"
        case .dateCreatedAsc: return "Date Created (Oldest First)"
        case .dueDateAsc: return "Due Date (Nearest First)"
        case .dueDateDesc: return "Due Date (Farthest First)"
        case .priorityHighToLow: return "Priority (High to Low)"
        case .priorityLowToHigh: return "Priority (Low to High)"
        case .titleAToZ: return "Title (A to Z)"
        case .titleZToA: return "Title (Z to A)"
        default: return "Unknown"
        }
    }

    var icon: String {
        switch self {
        case .dateCreatedDesc, .dateCreatedAsc:
            return "calendar"
        case .dueDateAsc, .dueDateDesc:
            return "calendar.badge.clock"
        case .priorityHighToLow, .priorityLowToHigh:
            return "flag.fill"
        case .titleAToZ, .titleZToA:
            return "textformat"
        default:
            return "arrow.up.arrow.down"
        }
    }
}
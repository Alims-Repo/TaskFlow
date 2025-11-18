//
//  TaskListState.swift
//  iosApp
//
//  Created by Abdul Alim Monshi on 11/19/25.
//
import Shared

enum TaskListState {
    case loading
    case success([Task])
    case error(String)
}
# 📱 TgLab Android Task

A modern Android application built using **Clean Architecture, MVVM, Jetpack Compose, Hilt, and Paging 3**.

## Features

- Browse NBA teams
- Sort teams (Name, City, Conference)
- View team games in a reusable bottom sheet
- Endless scrolling for games (Paging 3)
- Search players by name
- Open selected player's team games
- Proper loading, empty, and error state handling

The project focuses on clean code, separation of concerns, scalability, and production-ready patterns.

# 🏗 Architecture & Implementation

The project follows **Clean Architecture** with strict separation of concerns:


## Data Layer
- Retrofit + OkHttp for networking
- Kotlinx Serialization for JSON parsing
- Paging 3 for endless scrolling
- DTO → Domain mappers
- Repository implementations

## Domain Layer
- Pure Kotlin business logic
- UseCases (GetTeams, SearchPlayers)
- Repository contracts
- Unified error abstraction (`AppResult`, `AppError`)

## Presentation Layer
- MVVM pattern
- StateFlow-driven UI
- `UiState` for consistent UI state management
- Navigation Compose
- Modal Bottom Sheet shared across screens

## Error Handling Strategy

All errors follow a unified flow:
Throwable → AppError → UiState

This ensures:
- No raw exception messages shown to users
- Consistent error messaging
- Clear separation between data and UI




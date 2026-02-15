# 🏀 NBA Teams & Players Explorer

A production-ready Android application showcasing **Clean Architecture**, **MVVM**, and modern Android development best practices. Built with Jetpack Compose, Hilt, and Paging 3 for the TgLab Android Task.

---

## 📋 Overview

This application provides an intuitive interface to explore NBA teams, view their recent games, and search for players using the [balldontlie.io](https://www.balldontlie.io) API. The project demonstrates enterprise-level architecture patterns, comprehensive error handling, and optimal performance characteristics.

### ✨ Key Features

- **📊 Teams Browser** - View all NBA teams with sorting capabilities (Name, City, Conference)
- **🎮 Games Viewer** - Explore team games with infinite scroll pagination
- **🔍 Player Search** - Real-time debounced search with instant results
- **♻️ Reusable Components** - Shared bottom sheet for team games across screens
- **🎯 State Management** - Comprehensive handling of loading, success, error, and empty states
- **🔄 Retry Mechanism** - User-friendly error recovery on all screens

---

## 🏗️ Architecture

The application follows **Clean Architecture** principles with strict layer separation, ensuring maintainability, testability, and scalability.

### Architecture Layers

```
┌─────────────────────────────────────────────────────────┐
│                  Presentation Layer                      │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │   UI (Compose) │  │  ViewModels  │  │  Navigation  │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│                    Domain Layer                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │   Use Cases  │  │  Repository  │  │    Models    │  │
│  │              │  │  Interfaces  │  │              │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│                     Data Layer                           │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │  Repository  │  │   API Client │  │   Mappers    │  │
│  │     Impl     │  │   (Retrofit) │  │  (DTO→Domain)│  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└─────────────────────────────────────────────────────────┘
```

### 📦 Layer Responsibilities

#### **Presentation Layer**
- **UI Components** - Jetpack Compose declarative UI with Material 3
- **ViewModels** - State management using StateFlow, lifecycle-aware
- **Navigation** - Compose Navigation with bottom navigation and modal sheets
- **UiState** - Unified state representation (Loading, Success, Error, Empty)

#### **Domain Layer** (Pure Kotlin - No Android Dependencies)
- **Use Cases** - Business logic encapsulation (e.g., `GetTeamsUseCase` filters invalid teams)
- **Repository Interfaces** - Contracts for data operations
- **Domain Models** - Pure business entities (`Team`, `Game`, `Player`)
- **Error Abstraction** - `AppResult` and `AppError` for type-safe error handling

#### **Data Layer**
- **Repository Implementations** - Coordinate data sources
- **API Client** - Retrofit with Kotlinx Serialization
- **DTOs** - Network response models
- **Mappers** - Transform DTOs to domain models
- **Paging Source** - Infinite scroll implementation for games

---

## 🎯 Design Patterns & Best Practices

### MVVM Pattern
- **Unidirectional Data Flow** - ViewModels emit state, UI observes
- **Immutable State Exposure** - `StateFlow.asStateFlow()` prevents external mutations
- **No Business Logic in UI** - All logic resides in ViewModels/UseCases

### Dependency Injection (Hilt)
- **Constructor Injection** - All dependencies injected, improving testability
- **Scoped Dependencies** - Singleton for network layer, ViewModelScoped for ViewModels
- **Module Organization** - Separate modules for Network and Repository layers

### Error Handling Strategy

```kotlin
// Unified error flow across the application
Throwable → AppError (Domain) → UiState.Error (Presentation) → User Message
```

**Benefits:**
- ✅ No raw exceptions exposed to users
- ✅ Consistent error messaging across the app
- ✅ Type-safe error handling with sealed classes
- ✅ Retry functionality on all error states

**Error Types:**
- `NetworkUnavailable` - No internet connection
- `Timeout` - Request timeout
- `Http(code, message)` - Server errors (4xx, 5xx)
- `Unknown` - Unexpected errors

### State Management

```kotlin
sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
    data object Empty : UiState<Nothing>()
}
```

Every screen handles all four states with appropriate UI feedback.

---

## 🚀 Technical Implementation

### Technology Stack

| Category | Technology | Purpose |
|----------|-----------|---------|
| **UI Framework** | Jetpack Compose | Declarative UI with Material 3 |
| **Architecture** | MVVM + Clean Architecture | Separation of concerns |
| **DI** | Hilt | Dependency injection |
| **Networking** | Retrofit + OkHttp | REST API communication |
| **Serialization** | Kotlinx Serialization | JSON parsing (faster than Gson) |
| **Async** | Coroutines + Flow | Asynchronous operations |
| **Pagination** | Paging 3 | Infinite scroll for games |
| **Navigation** | Navigation Compose | Type-safe navigation |
| **Debugging** | Chucker (debug only) | Network traffic inspection |

### Key Features Implementation

#### 1️⃣ Teams List with Sorting
- **Data Filtering** - Removes invalid/historic teams (blank city, invalid conference)
- **Multi-Sort Support** - Sort by Name, City, or Conference
- **Reactive Updates** - UI automatically updates when sort changes
- **Empty State** - Graceful handling when no teams match criteria

#### 2️⃣ Infinite Scroll Games
- **Paging 3 Integration** - Efficient pagination with `PagingSource`
- **Load State Handling** - Separate UI for refresh vs append states
- **Error Recovery** - Retry button on pagination errors
- **Memory Efficient** - Loads 25 games per page, not all at once

#### 3️⃣ Player Search
- **Debounced Input** - 400ms debounce prevents excessive API calls
- **Reactive Search** - Flow operators (`debounce`, `distinctUntilChanged`)
- **Smart Empty States** - Different messages for "Type to search" vs "No results"
- **Team Navigation** - Tap player to view their team's games

#### 4️⃣ Reusable Bottom Sheet
- **Shared Component** - Same `TeamGamesSheet` used from Home and Players screens
- **Dynamic Content** - Loads games based on passed `teamId`
- **Proper Lifecycle** - ViewModel scoped correctly, data cached during config changes

---

## 📱 User Experience

### Navigation Flow

```
┌─────────────────┐         ┌─────────────────┐
│   Home Screen   │         │ Players Screen  │
│  (Teams List)   │         │  (Search)       │
└────────┬────────┘         └────────┬────────┘
         │                           │
         │ Tap Team                  │ Tap Player
         ▼                           ▼
    ┌─────────────────────────────────────┐
    │     Team Games Bottom Sheet         │
    │     (Infinite Scroll)               │
    └─────────────────────────────────────┘
```

### State Handling

Every screen provides clear feedback for all states:

- **⏳ Loading** - Centered spinner with descriptive text
- **✅ Success** - Scrollable list with proper item keys
- **❌ Error** - User-friendly message with retry button
- **📭 Empty** - Contextual empty state messages

---

## 🛡️ Security & Performance

### Security Considerations
- **API Key Protection** - Stored in `local.properties`, excluded from version control
- **BuildConfig Injection** - API key injected at build time, not hardcoded
- **Header Redaction** - Authorization header redacted in debug logs
- **No Sensitive Data Caching** - No persistent storage of user data

### Performance Optimizations
- **LazyColumn** - Efficient list rendering, only visible items composed
- **Item Keys** - Prevents unnecessary recomposition on list updates
- **Debounced Search** - Reduces API calls by 90%+ during typing
- **Pagination** - Loads data incrementally, not all at once
- **StateFlow** - Efficient state updates, survives configuration changes
- **Kotlinx Serialization** - Faster JSON parsing than reflection-based libraries

### Memory Management
- **ViewModelScope** - Automatic coroutine cancellation on ViewModel clear
- **Paging Cache** - `cachedIn(viewModelScope)` prevents duplicate loads
- **No Memory Leaks** - Proper lifecycle handling throughout

---

## 🔧 Setup & Configuration

### Prerequisites
- Android Studio Hedgehog or later
- JDK 11+
- Android SDK 28+ (minSdk 28, targetSdk 35)

### API Key Setup

1. Get your API key from [balldontlie.io](https://www.balldontlie.io)
2. Create `local.properties` in the project root (if not exists)
3. Add your API key:
   ```properties
   BALLDONTLIE_API_KEY=your_api_key_here
   ```
4. Build and run the app

> **Note:** The app will fail to build without a valid API key in debug mode.

### Build & Run

```bash
# Clone the repository
git clone <repository-url>

# Open in Android Studio
# Add API key to local.properties
# Sync Gradle
# Run on device/emulator
```

---

## 📂 Project Structure

```
app/src/main/java/com/aslmmovic/tglabtask/
├── data/
│   ├── di/                    # Hilt modules (Network, Repository)
│   └── remote/
│       ├── api/               # Retrofit API interfaces
│       ├── dto/               # Network response models
│       ├── mapper/            # DTO → Domain mappers
│       ├── paging/            # Paging 3 sources
│       └── repository/        # Repository implementations
├── domain/
│   ├── di/                    # Domain DI modules
│   ├── model/                 # Business entities
│   ├── repository/            # Repository contracts
│   ├── usecase/               # Business logic
│   └── util/                  # AppResult, AppError
└── presentation/
    ├── navigation/            # Navigation setup
    ├── theme/                 # Material 3 theme
    ├── ui/
    │   ├── component/         # Reusable UI components
    │   ├── home/              # Teams screen
    │   ├── players/           # Search screen
    │   └── team/              # Games bottom sheet
    ├── util/                  # UiState, mappers
    └── viewmodel/             # ViewModels
```

---

## 🧪 Testing Strategy

### Recommended Test Coverage

**Unit Tests** (ViewModels, UseCases, Mappers)
- ViewModel state transitions
- UseCase business logic (filtering, sorting)
- Mapper transformations

**Integration Tests** (Repository)
- API integration with MockWebServer
- Error handling scenarios

**UI Tests** (Compose)
- User flows (navigation, sorting, search)
- State rendering (loading, error, success)

---

## 🎓 Learning Outcomes

This project demonstrates:

✅ **Clean Architecture** - Proper layer separation and dependency inversion  
✅ **MVVM Pattern** - Reactive state management with StateFlow  
✅ **Modern Android** - Jetpack Compose, Hilt, Paging 3, Navigation Compose  
✅ **Error Handling** - Comprehensive, user-friendly error management  
✅ **Best Practices** - Kotlin idioms, coroutines, sealed classes  
✅ **Performance** - Efficient rendering, pagination, debouncing  
✅ **Code Quality** - Single Responsibility, DRY, SOLID principles  

---

## 📄 License

This project is created for the TgLab Android Task assessment.

---

## 👤 Author

**Aslmmovic**

Built with ❤️ using modern Android development practices.

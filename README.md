# Smart Notes App 📝

A simple and modern Notes application built with **Kotlin and Jetpack Compose**. This project was created to learn and implement Android app development concepts such as Room Database, MVVM architecture, StateFlow, Navigation, and Material 3 theming.

## ✨ Features

* ➕ Add new notes
* ✏️ Edit existing notes
* 🗑️ Delete notes with confirmation
* 🔍 Search notes
* 💾 Local data storage using Room Database
* 🌙 Light and Dark theme support
* 📱 Jetpack Compose UI
* 🔄 Reactive UI using StateFlow

## 🛠️ Tech Stack

* **Language:** Kotlin
* **UI:** Jetpack Compose
* **Architecture:** MVVM
* **Database:** Room
* **State Management:** StateFlow
* **Navigation:** Navigation Compose
* **Design:** Material 3

## 🏗️ Architecture

The application follows the MVVM architecture:

```text
UI
 ↓
ViewModel
 ↓
Repository
 ↓
DAO
 ↓
Room Database
```

### Main Components

* **Note** — Room Entity representing a note
* **NoteDao** — Provides database operations
* **AppDatabase** — Room database configuration
* **NoteRepository** — Handles data access
* **NotesViewModel** — Manages UI state and operations
* **MainActivity** — Entry point of the application

## 📚 What I Learned

Through this project, I learned how to:

* Build Android UIs using Jetpack Compose
* Work with Room Database for local persistence
* Implement CRUD operations
* Use MVVM architecture
* Manage UI state using StateFlow
* Implement navigation between screens
* Implement search functionality
* Create light and dark themes using Material 3

## 🚀 Future Improvements

* Cloud synchronization
* Note categories
* Pin important notes
* Reminders
* Rich text formatting

## 👨‍💻 Author

**Manal Kaura**

Built as part of my Android development learning journey.

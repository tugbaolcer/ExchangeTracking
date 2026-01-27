# ExchangeTracking 📈

**ExchangeTracking** is a modern Android application designed to track real-time currency exchange rates and financial data. The project is built with a focus on scalability, maintainability, and performance using **Clean Architecture** principles.

---


📸 Screenshots



[Screen_recording_20260127_152818.webm](https://github.com/user-attachments/assets/017bcbc9-0932-4159-87f2-a101d3d27bcc)

## 🚀 Features
- **Real-time Updates:** Live tracking of exchange rates using **WebSocket** integration.
- **Data Source:** Powered by **Binance Futures WebSocket** (`wss://fstream.binance.com`).
- **Modern UI:** A fully declarative and responsive user interface built with **Jetpack Compose**.
- **Efficiency:** Optimized network communication using the **Ktor** client.
- **Robust Architecture:** Clean and modular code structure for professional-grade development.

---

## 🛠 Tech Stack & Libraries

### Architecture
- **Clean Architecture:** Divided into **Domain**, **Data**, and **Presentation** layers to ensure separation of concerns and testability.
- **MVVM (Model-View-ViewModel):** Decouples UI logic from business logic.

### UI & Design
- **Jetpack Compose:** Android’s modern toolkit for building native UI.
- **Kotlin Coroutines & Flow:** For managing asynchronous data streams and reactive UI updates.

### Network
- **Ktor Client:** A lightweight and flexible asynchronous HTTP client.
- **WebSockets:** Utilized via Ktor for continuous, bi-directional communication with the server.
- **Kotlinx Serialization:** For efficient JSON parsing into Kotlin objects.

### Dependency Injection & Navigation
- **Dagger Hilt:** Standard library for Dependency Injection (DI) in Android.
- **Navigation Compose:** Type-safe navigation between screens.

---

## 🏗 Project Structure
```text
app/
├── data/           # API services, Repository implementations, and Data models
├── domain/         # Use Cases, Repository interfaces, and Domain entities
├── presentation/   # UI components (Compose), ViewModels, and UI State
└── di/             # Hilt Modules (Dependency Injection)
```

🤝 Contributing
1. Fork the project.

2. Create your feature branch (git checkout -b feature/AmazingFeature).

3. Commit your changes (git commit -m 'Add some AmazingFeature').

4. Push to the branch (git push origin feature/AmazingFeature).

5. Open a Pull Request.

📝 License
This project is licensed under the MIT License - see the LICENSE file for details.

Developer: Tuğba Ölçer




# C-Kernel Sim — Interactive C Programming Simulator

A native Android application built with **Kotlin** and **Jetpack Compose (Material 3)**. C-Kernel Sim visualizes low-level C programming concepts including dynamic memory allocation, stack frames, pointer arithmetic, loops, structs, and real-time kernel anomaly debugging.

---

## 🚀 Pre-compiled APK Download

A ready-to-install debug APK is available directly in this repository:
- **Location**: [`apk/c-kernel-sim-debug.apk`](apk/c-kernel-sim-debug.apk)
- **Target SDK**: Android 7.0 (API 24) to Android 16 (API 36)

To install on your phone:
1. Download `c-kernel-sim-debug.apk` to your Android device.
2. Tap the file in your Downloads/Files app to install.
3. Enable "Install unknown apps" if prompted.

---

## 🛠️ Building from Source

### Prerequisites
- **Android Studio** (Ladybug, Koala, Hedgehog, or newer)
- **JDK 17** (or Android Studio bundled JDK)

### Steps
1. Clone this repository:
   ```bash
   git clone <your-repo-url>
   cd <repo-folder>
   ```
2. Open the project in Android Studio:
   - Select **File > Open...** and choose the root folder.
   - Wait for Gradle sync to complete.
3. Build the APK:
   - In Android Studio: **Build > Build Bundle(s) / APK(s) > Build APK(s)**
   - Or from terminal:
     ```bash
     ./gradlew assembleDebug
     ```
4. Output APK location:
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

---

## 🌟 Key Features
- **9 Progressive Simulation Levels**: Variables, Conditionals, Arithmetic, Loops, Functions & Stack Frames, Pointers & Addresses, Dereferencing, Dynamic Heap (`malloc`/`free`), and Structs.
- **Animated Architecture Visualizers**: Real-time RAM Memory Grid & LIFO Call Stack.
- **Interactive Kernel Anomaly Events**: Neutralize SIGSEGV, Buffer Overflows, and Memory Leaks.
- **Achievements & Badges System**: Unlock bronze, silver, gold, diamond, and legendary badges with XP tracking.
- **100% Offline**: All simulations and progress state run locally on the device.

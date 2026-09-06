# C-Kernel Sim — Interactive C Programming Simulator

A native Android application built with **Kotlin** and **Jetpack Compose (Material 3)**. C-Kernel Sim visualizes low-level C programming concepts including dynamic memory allocation, stack frames, pointer arithmetic, loops, structs, and real-time kernel anomaly debugging.

---

## 🚀 APK Download & Installation

### Option 1: Download from GitHub Actions (Recommended)
Every commit automatically triggers a clean build via GitHub Actions CI:
1. Click the **Actions** tab at the top of your GitHub repository.
2. Click on the latest workflow run under **Android CI Build**.
3. Scroll down to the **Artifacts** section at the bottom of the summary page.
4. Click on **`app-debug-apk`** to download the ZIP file containing `app-debug.apk`.
5. Transfer the APK to your Android phone and install it!

### Option 2: Build Locally in Android Studio
1. Clone this repository or download as ZIP.
2. Open the project folder in **Android Studio**.
3. Go to **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
4. Your APK will be located at:
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

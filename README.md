# 🚗 Vehicle Number Plate Detection

A real-time Android application that detects and reads vehicle license plate numbers using the device camera — fully on-device, no internet required.

![Demo](art/detection.gif)

---

## ✨ Features

- **Real-time detection** — Live camera feed with instant plate recognition
- **On-device processing** — No server, no cloud, works fully offline
- **Regex validation** — Filters out noise using license plate format patterns
- **CameraX integration** — Modern, lifecycle-aware camera API
- **ML Kit Text Recognition** — Google's on-device OCR engine for accurate text extraction
- **Customizable** — Easily adapt regex patterns for different country plate formats

---

## 🛠 Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| Camera | CameraX |
| Text Recognition | ML Kit (Text Recognition v2) |
| Plate Validation | Regex pattern matching |
| Min SDK | Android 5.0 (API 21)+ |

---

## 📐 How It Works

1. **CameraX** opens a live preview and feeds frames to an `ImageAnalysis` use case
2. Each frame is passed to **ML Kit's Text Recognizer**, which extracts all text blocks
3. Extracted strings are matched against **license plate regex patterns** (e.g. Indian format: `[A-Z]{2}[0-9]{2}[A-Z]{1,2}[0-9]{4}`)
4. Matched plates are highlighted and displayed in real time on screen

---

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog or newer
- Android device or emulator running **API 21+**
- Camera permission granted at runtime

### Clone & Run

```bash
git clone https://github.com/aahanverma00710/VehicleNumberPlateDetection.git
cd VehicleNumberPlateDetection
```

1. Open the project in **Android Studio**
2. Click **Sync Now** when prompted
3. Run on a physical device or emulator with camera support

---

## 🔧 Customization

To support different country plate formats, update the regex in the analyzer:

```kotlin
// Example: Indian plate format
val plateRegex = Regex("[A-Z]{2}[0-9]{2}[A-Z]{1,2}[0-9]{4}")

// Example: UK plate format
val plateRegex = Regex("[A-Z]{2}[0-9]{2}\\s?[A-Z]{3}")
```

---

## 📁 Project Structure

```
app/src/main/java/
└── com.avcoding.vehiclenumberplatedetection/
    ├── MainActivity.kt          # Camera setup & lifecycle
    ├── PlateAnalyzer.kt         # ImageAnalysis + ML Kit + Regex logic
    └── utils/
        └── PlateValidator.kt    # Regex pattern definitions
```

---

## 📋 Permissions

```xml
<uses-permission android:name="android.permission.CAMERA" />
```

Runtime permission is requested automatically on first launch.

---

## 🔮 Possible Extensions

- Save detected plates to a local database (Room)
- Add multi-country plate format support
- Integrate with a vehicle lookup API
- Add plate detection history with timestamps

---

## 📬 Author

**Aahan Verma** — Senior Android Developer  
[![GitHub](https://img.shields.io/badge/GitHub-aahanverma00710-181717?style=flat&logo=github)](https://github.com/aahanverma00710)
[![Twitter](https://img.shields.io/badge/Twitter-@aahanverma00710-1DA1F2?style=flat&logo=twitter)](https://twitter.com/aahanverma00710)

---

## 📄 License

```
Copyright 2024 Aahan Verma

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0
```

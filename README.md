# 🏥 Arogya-Nidhi
### Health Scheme Eligibility Checker — Android App
**Android • Kotlin • Healthcare • Social Impact**

---

## 📌 Overview
**Arogya-Nidhi** is a mobile application designed to bridge the information gap between citizens and government health schemes in India.

Many rural and low-income families are unaware of schemes they are eligible for, leading to unnecessary medical expenses. This app acts as a **digital health counselor**, helping users instantly discover eligible schemes and required documents.

---

## 🎯 Problem Statement
- Lack of awareness about government health schemes
- Families visiting offices without proper documents
- High out-of-pocket medical expenses
- Complex government portals not accessible to low-literacy users

---

## 💡 Solution
Arogya-Nidhi simplifies the process by:
- Providing an **eligibility quiz**
- Displaying **eligible schemes instantly**
- Showing **document checklists**
- Listing **nearby empaneled hospitals**

---

## 👥 Target Users
- Rural & semi-urban families
- BPL cardholders
- Daily wage workers
- Citizens unaware of health entitlements

---

## 🚀 Features

### ✅ Must Have
- Eligibility Quiz (Stepper UI)
- Scheme Result Screen
- Document Checklist per Scheme
- Hospital List with District Search
- Offline Mode (No Internet Required)
- Input Validation

### ⭐ Good to Have
- Save Results as PDF / Share
- GenAI Chatbot
- Multilingual Support
- Google Maps Integration
- Dark Mode

---

## 🛠️ Tech Stack
- **Language:** Kotlin
- **Platform:** Android
- **UI:** XML / Jetpack Compose (Material Design 3)
- **Database:** Room DB
- **Data:** JSON (stored locally)
- **Build Tool:** Gradle
- **Min SDK:** API 26

## 📂 Project Structure

<pre>
app/
├── manifests/
│   └── AndroidManifest.xml
├── java/com/arogyanidhi/app/
│   ├── MainActivity.kt
│   ├── data/
│   │   ├── models/ (Models.kt)
│   │   └── repository/ (AuthRepository.kt, DataRepository.kt)
│   ├── navigation/ (NavGraph.kt, Screen.kt)
│   └── ui/
│       ├── screens/ (Splash, Login, Quiz, Results, etc.)
│       └── theme/ (Theme.kt, Typography.kt)
├── res/values/ (strings.xml, themes.xml)
└── Gradle Scripts/
    ├── build.gradle.kts
    └── libs.versions.toml
</pre>

## 🖼️ Screenshots


### Quiz Screen
![Quiz](Images/1.png)
![](Images/2.png)
![](Images/3.png)
![](Images/4.png)
![](Images/5.png)

### Result Screen
![Result](Images/6.png)
![](Images/7.png)


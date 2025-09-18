# 💰 Android Expense Tracker App

An **Android Expense Tracker** built with modern Android development practices.  
This app demonstrates **clean architecture, Jetpack Compose UI, and Room persistence**, along with **report generation and PDF sharing**.

---

## 🚀 Tech Stack
- **Kotlin** – main programming language  
- **Jetpack Compose** – modern declarative UI  
- **MVVM Architecture** – separation of concerns & testability  
- **Room Database** – local storage for expenses  
- **Hilt (Dagger)** – dependency injection  
- **Jetpack Navigation** – screen transitions and state handling  
- **Coroutines + StateFlow** – reactive data flows  
- **Material 3 Components** – modern UI styling  

---

## 📱 Screens & Flows

### 1. Expense Entry Screen
- Input fields:
  - **Title** (text)  
  - **Amount** (₹)  
  - **Category** (mocked list: *Staff, Travel, Food, Utility*)  
  - **Optional Notes** 
  - **Optional Receipt Image**  
- **Submit Button**: adds expense, shows Toast, animates entry  
- Shows **real-time “Total Spent Today”** at the top  

---

### 2. Expense List Screen
- Default view: **Today’s expenses**  
- Filter by **previous dates** (calendar / filter)  
- **Group by category or date** (toggle)  
- Displays:
  - **Total count**  
  - **Total amount**  
  - **Empty state UI** if no data  

---

### 3. Expense Report Screen
- **Mock report for last 7 days**:
  - Daily totals  
  - Category-wise totals  
  - Bar chart  
- **Export options**:
  - Simulate **PDF export**  
  - Trigger **Share intent**  

---

## 🗂️ State Management & Data Layer
- **ViewModel + StateFlow** for reactive state  
- **Room Database** for local persistence  
- **Hilt** for dependency injection  
- **Navigation Component** for seamless screen transitions  

---

## 🤖 AI Assistance in Development
This project also explored **AI-driven productivity**:  
- 🏗️ **Architecture Guidance** – used AI to refine MVVM + Navigation setup  
- 📄 **PDF Report Generation** – auto-generate expense reports with charts and category totals  
- ⚡ **Code Optimization** – AI helped optimize navigation logic and database migrations  
- 📝 **Documentation** – AI-generated README and code explanations  

---

## 📦 Installation
1. Clone the repo:
   ```bash
   SSH : git@github.com:Omkar-Shinde-97/Expense-Tracker.git
   http : https://github.com/Omkar-Shinde-97/Expense-Tracker.git

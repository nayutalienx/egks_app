# EGKS Transport Card Manager

An Android application for checking public transport card balances and managing multiple cards. Originally developed as a university project for web design coursework, this app provides a convenient interface for monitoring transport card balances in real-time.

## Features

- **Multiple Card Management**: Add, edit, and delete multiple transport cards
- **Real-time Balance Checking**: Check current balance and transaction history
- **Card Information Storage**: Securely store card names and numbers locally
- **Modern UI**: Clean Material Design interface with navigation drawer
- **Balance Updates**: Automatic balance refresh with loading animations
- **Offline Storage**: Card data persisted locally using PaperDB

## Technology Stack

- **Platform**: Android (API 21+)
- **Language**: Java
- **Architecture**: Traditional Android with Fragments
- **Networking**: Retrofit 2 for API calls
- **Local Storage**: PaperDB for data persistence
- **UI Components**: Android Support Library v28
- **Animations**: Custom loading button animations

## App Structure

The application consists of several key components:

- **MainActivity**: Main entry point with navigation drawer
- **MainFragment**: Home screen displaying card balance
- **ManagerFragment**: Card management interface
- **CardListAdapter**: Custom adapter for card list display
- **ManagerDialogAdd/Edit**: Dialogs for adding and editing cards
- **Balance**: Widget for home screen balance display

## API Integration

The app integrates with transport card services through:
- REST API calls to balance checking service
- Real-time balance retrieval
- Transaction history access
- Card validation

## Screenshots

### Main Screen - Card Balance Display
<img src="https://github.com/nayutalienx/egks_app/blob/master/1.png" alt="Main screen showing transport card balance" width="250">

### Navigation Menu
<img src="https://github.com/nayutalienx/egks_app/blob/master/2.png" alt="Navigation drawer menu" width="250">

### Card Manager
<img src="https://github.com/nayutalienx/egks_app/blob/master/3.png" alt="Card management interface" width="250">

### Online Payment Interface
<img src="https://github.com/nayutalienx/egks_app/blob/master/4.png" alt="Online payment and top-up interface" width="250">

## Setup and Installation

### Prerequisites
- Android Studio 3.4+
- Android SDK API 28
- Java 8+

### Building the Project
1. Clone the repository:
   ```bash
   git clone https://github.com/nayutalienx/egks_app.git
   ```

2. Open the project in Android Studio

3. Sync Gradle files

4. Run the application on an emulator or physical device

### Dependencies
The project uses the following key dependencies:
- `retrofit2` - HTTP client for API calls
- `paperdb` - NoSQL database for local storage
- `loading-button-android` - Animated loading buttons
- Android Support Library v28

## Usage

1. **Adding a Card**: Use the "+" button to add a new transport card with name and number
2. **Checking Balance**: Select a card from the dropdown and tap "Update" to check current balance
3. **Managing Cards**: Access the card manager through the navigation menu to edit or delete cards
4. **Online Payment**: Use the payment interface to top up card balance online

## Project Background

This application was originally developed as a university project for web design coursework, focusing on creating a practical mobile solution for public transport users. The app demonstrates modern Android development practices including REST API integration, local data persistence, and Material Design principles.

## License

This project was created for educational purposes as part of university coursework.

# 🏋️‍♂️ Sportify - Fitness Management Platform

Sportify is a comprehensive JavaFX-based fitness management platform that serves both users and gym administrators with an integrated web application built on Symfony for shared functionality.

[![Java Version](https://img.shields.io/badge/Java-17%2B-orange)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![JavaFX](https://img.shields.io/badge/JavaFX-17%2B-blue)](https://openjfx.io/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)](https://www.mysql.com/)
[![Symfony](https://img.shields.io/badge/Symfony-6.0-black)](https://symfony.com/)

## 📋 Project Overview

Sportify is a dual-interface fitness management platform that connects gym administrators and users through both a JavaFX desktop application and a Symfony web application sharing the same database. The platform offers comprehensive fitness management tools including post sharing, user management, equipment tracking, gym facility management, subscription services, product sales, and more.

## 🚀 Key Features

### 👥 User Management
- User registration and authentication with secure password hashing
- Role-based access control (Admin, User, Coach)
- Profile management and user settings
- Account verification via SMS with Twilio integration

### 📱 Social Platform
- Create, view, edit, and delete posts categorized by type (Marathon, Promotion, Diet)
- Comment system with real-time updates
- Like functionality for posts and comments
- Image upload and management
- Telegram integration for post notifications

### 💪 Fitness Management
- Gym equipment catalog and management
- Exercise library with instruction videos
- Sports facility tracking and booking
- Workout regimens and plans

### 💰 Business Operations
- Subscription management with multiple membership types
- Product catalog and sales
- Payment processing with Stripe integration
- Promotion system
- Order management

### 📊 Analytics & AI
- User statistics and reports
- AI-powered product recommendations using OpenAI integration
- Personalized fitness advice based on user profiles

## 🔧 Technical Stack

### Back-End
- **Java 17+**: Core application logic
- **MySQL**: Main database shared with Symfony
- **Hibernate/JPA**: ORM for database operations
- **API Integrations**:
  - OpenAI (GPT-3.5): Smart product recommendations
  - Twilio: SMS services for verification
  - Telegram Bot API: Notification system
  - Stripe: Payment processing

### Front-End
- **JavaFX**: Desktop UI framework
- **FXML**: UI layout definition
- **CSS**: Custom styling and theming

### Tools & Libraries
- **Scene Builder**: UI design
- **Maven**: Dependency management
- **BCrypt**: Password hashing

## 🏗️ Architecture

The application follows a modified MVC (Model-View-Controller) architecture:
- **Models**: Entity classes in `entities` package
- **Views**: FXML files in `resources` directory
- **Controllers**: Controller classes in `gui` package
- **Services**: Business logic in `services` package
- **Utils**: Helper functions and API wrappers in `utils` package

## 📚 Package Overview

- **entities**: Data models (User, Post, Comment, Product, etc.)
- **services**: Business logic and database operations
- **utils**: Helper classes, API integrations, session management
- **gui**: JavaFX controllers and UI logic
- **resources**: FXML layouts, stylesheets, and assets

## 🔄 Shared Database with Symfony

The platform uses a shared database architecture where:
- Both JavaFX and Symfony applications connect to the same MySQL database
- Entity definitions are synchronized between Java and PHP (Doctrine)
- File uploads are stored in a common directory accessible to both platforms

## 🛠️ Installation and Setup

### Prerequisites
- Java 17 or higher
- MySQL 8.0 or higher
- Maven

### Setup Steps
1. Clone the repository
2. Configure your database connection in `MyConnection.java`
3. Install required dependencies: `mvn install`
4. Run the application: `mvn javafx:run`

### API Configuration
- Configure your API keys in the appropriate utility classes:
  - OpenAI: `DeepSeekService.java`
  - Twilio: `SmsService.java`
  - Telegram: `TelegramBot.java`
  - Stripe: `StripeConfig.java`

## 📊 Database Schema

The database includes tables for:
- Users and authentication
- Posts and comments
- Products and orders
- Subscriptions and promotions
- Gyms, equipment, and exercises
- And more...

## 🔗 Integration with Symfony

For the Symfony web application:
1. Files are saved to a shared directory accessible by both applications
2. Database schema is designed to be compatible with both platforms
3. Authentication tokens and sessions can be interchanged between systems

## 📝 License

This project is for educational purposes as part of a development project.

## #️⃣ Tags

#Java #JavaFX #Symfony #MySQL #Fitness #GymManagement #TwilioAPI #TelegramAPI #StripePayments #OpenAI #FXML #CSS #JavaProgramming #DesktopApplication #WebApplication 

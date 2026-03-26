# 🛒 E-Commerce Application

A comprehensive desktop e-commerce application built with **Java** and **JavaFX**, featuring a modern UI and complete shopping functionality for multiple user roles.

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-007396?style=for-the-badge&logo=java&logoColor=white)

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Architecture](#️-architecture)
- [Class Diagram](#-class-diagram)
- [User Roles](#-user-roles)
- [Screenshots](#-screenshots)
- [Demo Video](#-demo-video)
- [Installation](#-installation)
- [Usage](#-usage)
- [Project Structure](#-project-structure)
- [Technologies Used](#️-technologies-used)
- [Contributing](#-contributing)
- [License](#-license)

---

## 🌟 Overview

This E-Commerce Application is a full-featured desktop shopping platform that supports three distinct user roles: **Admin**, **Seller**, and **Customer**. The application provides a complete e-commerce experience including product browsing, shopping cart management, order processing, and comprehensive reporting features.

The application uses **file-based persistence** to store data locally, making it easy to deploy and run without requiring a database server.

---

## ✨ Features

### 🔐 Authentication & User Management
- User registration with role selection (Admin/Seller/Customer)
- Secure login system with password validation
- Default admin account creation on first run
- User profile management and updates

### 🛍️ Shopping Features
- Browse products with search and filter capabilities
- Add products to shopping cart
- Adjust quantities in cart
- Checkout and place orders
- View order history and status
- Payment processing

### 👨‍💼 Admin Features
- Complete product management (CRUD operations)
- User management and oversight
- Product reports and analytics
- View all orders across the platform
- Supplier management and pricing reports

### 🏪 Seller Features
- Add and manage own products
- View and manage inventory
- Track product sales
- Revenue reports and analytics
- Date-range based revenue calculations
- View orders for their products

### 👤 Customer Features
- Browse product catalog
- Shopping cart management
- Place orders
- View order history
- Update profile information

### 📊 Reporting & Analytics
- Product stock reports
- Out-of-stock product alerts
- Revenue tracking (total and average)
- Supplier pricing reports
- Date-range based analytics

---

## 🏗️ Architecture

The application follows a **layered architecture** pattern:

```
┌─────────────────────────────────────┐
│         UI Layer (JavaFX)           │
│  - Login/Register                   │
│  - Dashboards (Admin/Seller/Customer)│
│  - Product Management               │
│  - Order Views                      │
└─────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────┐
│       Domain/Business Logic         │
│  - User Management                  │
│  - Product Management               │
│  - Shopping Cart                    │
│  - Order Processing                 │
└─────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────┐
│         Data Layer                  │
│  - DataStore (Singleton)            │
│  - FileManager                      │
│  - Serialization                    │
└─────────────────────────────────────┘
```

### Key Design Patterns Used:
- **Singleton Pattern**: DataStore ensures single instance for data management
- **Factory Pattern**: ID generation for users, products, and orders
- **Strategy Pattern**: Different dashboards for different user roles

---

## 📐 Class Diagram

The following UML class diagram illustrates the structure and relationships between all classes in the application:

![Class Diagram](<img width="3961" height="3495" alt="E-Com (FINAL) drawio" src="https://github.com/user-attachments/assets/44092f91-5f46-4565-b62b-92b98747a128" />
)

📄 **[View Full Class Diagram (PDF)]([E-Com (FINAL).drawio.pdf](https://github.com/user-attachments/files/24976672/E-Com.FINAL.drawio.pdf)
)**

### Key Components:

#### **Domain Layer**
- **User Hierarchy**: Abstract `User` class with `Admin`, `Seller`, and `Customer` subclasses
- **Shopping System**: `Product`, `Cart`, `Order`, `Item`, and `Supplier` classes
- **Relationships**: 
  - Customer has-a Cart
  - Cart contains multiple Items
  - Order contains multiple Products
  - Seller manages multiple Products

#### **Data Layer**
- **DataStore**: Singleton pattern for centralized data management
- **FileManager**: Handles serialization and file I/O operations

#### **UI Layer**
- **MainApp**: Application entry point
- **Role-specific Dashboards**: AdminDashboard, SellerDashboard, CustomerDashboard
- **Management Views**: Product management, order management, reports

#### **Utilities**
- **IDGenerator**: Atomic ID generation for entities
- **UIStyles**: Centralized styling constants

---

## 👥 User Roles

### 1. Admin 👨‍💼
- **Full system access**
- Manage all products across all sellers
- View and manage all users
- Access comprehensive reports
- Supplier management
- **Default Credentials**:
  - Username: `admin`
  - Password: `admin`

### 2. Seller 🏪
- Manage own products
- View sales analytics
- Track revenue
- Update inventory
- View orders for their products

### 3. Customer 🛍️
- Browse and search products
- Manage shopping cart
- Place and track orders
- Update profile
- View order history

---

## 📸 Screenshots

### Login Screen
> The modern login interface with role selection

![Login Screen](path/to/login-screenshot.png)

---

### Admin Dashboard
> Admin dashboard showing product management and analytics

![Admin Dashboard](path/to/admin-dashboard-screenshot.png)

---

### Product Catalog
> Product browsing interface with search functionality

![Product Catalog](path/to/product-catalog-screenshot.png)

---

### Shopping Cart
> Shopping cart with items and checkout option

![Shopping Cart](path/to/cart-screenshot.png)

---

### Seller Dashboard
> Seller's product management and revenue analytics

![Seller Dashboard](path/to/seller-dashboard-screenshot.png)

---

### Order Management
> Order history and tracking interface

![Order Management](path/to/order-management-screenshot.png)

---

## 🎥 Demo Video

> A comprehensive walkthrough of the application

[![E-Commerce Application Demo](path/to/video-thumbnail.png)](path/to/demo-video.mp4)

### What's in the Demo:
- User registration and login process
- Admin product management
- Seller adding products
- Customer browsing and shopping
- Cart management and checkout
- Order processing and tracking
- Revenue reports and analytics

---

## 🚀 Installation

### Prerequisites
- **Java JDK 11** or higher
- **JavaFX SDK** (if not bundled with your JDK)
- **IDE** (NetBeans, IntelliJ IDEA, or Eclipse recommended)

### Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/Mahmoud7111/E-Commerce-Application.git
   cd E-Commerce-Application
   ```

2. **Set up JavaFX** (if needed)
   - Download JavaFX SDK from [openjfx.io](https://openjfx.io/)
   - Configure your IDE to include JavaFX libraries

3. **Open in your IDE**
   - Open the project folder in your Java IDE
   - Ensure all dependencies are resolved

4. **Build the project**
   ```bash
   # Using Maven (if configured)
   mvn clean install
   
   # Or build through your IDE
   ```

5. **Run the application**
   - Run the `MainApp.java` file located in `ecommerce/UI/`
   - Or use:
   ```bash
   java -jar E-Commerce-Application.jar
   ```

6. **First Run**
   - The application will automatically create a `dataFolder` directory
   - A default admin account will be created automatically:
     - Username: `admin`
     - Password: `admin`

---

## 💻 Usage

### For Admins:
1. Log in with admin credentials
2. Navigate to Product Management to add/edit/delete products
3. View reports for analytics
4. Manage users and suppliers

### For Sellers:
1. Register as a seller with store name
2. Add your products through the product management interface
3. Monitor your revenue and sales
4. Update product inventory

### For Customers:
1. Register as a customer with shipping details
2. Browse the product catalog
3. Add items to cart
4. Proceed to checkout
5. Track your orders

---

## 📁 Project Structure

```
E-Commerce-Application/
│
├── ecommerce/
│   ├── UI/                           # JavaFX User Interface
│   │   ├── MainApp.java              # Application entry point
│   │   ├── LoginMenu.java            # Login interface
│   │   ├── RegisterMenu.java         # Registration interface
│   │   ├── UIStyles.java             # Centralized styling
│   │   └── dashboards/               # Role-specific dashboards
│   │       ├── Admin/
│   │       │   ├── AdminDashboard.java
│   │       │   ├── AdminProductManagement.java
│   │       │   └── AdminProductReports.java
│   │       ├── Seller/
│   │       │   ├── SellerDashboard.java
│   │       │   └── SellerOrderReports.java
│   │       └── CustomerDashboard.java
│   │
│   ├── domain/                       # Business logic layer
│   │   ├── users/                    # User management
│   │   │   ├── User.java            # Abstract user class
│   │   │   ├── Admin.java
│   │   │   ├── Seller.java
│   │   │   └── Customer.java
│   │   └── Shopping/                 # Shopping functionality
│   │       ├── Product.java
│   │       ├── Cart.java
│   │       ├── Order.java
│   │       ├── Item.java
│   │       └── Supplier.java
│   │
│   ├── data/                         # Data persistence layer
│   │   ├── DataStore.java           # Singleton data manager
│   │   └── FileManager.java         # File I/O operations
│   │
│   └── util/                         # Utility classes
│       └── IDGenerator.java         # ID generation utility
│
└── dataFolder/                       # Generated at runtime
    ├── users.dat                     # Serialized user data
    ├── products.dat                  # Serialized product data
    ├── carts.dat                     # Serialized cart data
    └── orders.dat                    # Serialized order data
```

---

## 🛠️ Technologies Used

- **Java 11+**: Core programming language
- **JavaFX**: Modern UI framework for desktop applications
- **Java Serialization**: Data persistence mechanism
- **File I/O**: Local data storage
- **Atomic Operations**: Thread-safe ID generation
- **Observer Pattern**: UI updates and event handling

---

## 🎨 UI Features

- **Modern Material Design inspired interface**
- **Color-coded buttons and actions**
  - Primary: Blue (#2196F3)
  - Success: Green (#4CAF50)
  - Warning: Orange (#FF9800)
  - Error: Red (#F44336)
- **Responsive layouts**
- **Hover effects and visual feedback**
- **Card-based design for content organization**
- **Search and filter functionality**
- **Table views with sortable columns**

---

## 🔄 Data Persistence

The application uses **Java Serialization** for data persistence:

- All data is stored in the `dataFolder` directory
- Data is automatically saved on every modification
- Singleton pattern ensures data consistency
- Automatic initialization on first run

### Data Files:
- `users.dat`: Stores all user accounts
- `products.dat`: Stores product catalog
- `carts.dat`: Stores shopping carts
- `orders.dat`: Stores order history

---

## 🔐 Security Features

- Password validation (minimum 4 characters)
- Role-based access control
- Input validation for all forms
- Email format validation
- Safe type casting for user roles
- Automatic data persistence

---

## 🐛 Known Issues

- No issues reported yet! This is a newly created repository.

---

## 🚧 Future Enhancements

- [ ] Add database support (MySQL/PostgreSQL)
- [ ] Implement payment gateway integration
- [ ] Add product images
- [ ] Email notifications for orders
- [ ] Advanced search with filters
- [ ] Product categories and subcategories
- [ ] Wishlist functionality
- [ ] Product reviews and ratings
- [ ] Export reports to PDF/Excel
- [ ] Multi-language support

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is currently **unlicensed**. Please contact the repository owner for usage permissions.

---

## 👨‍💻 Author

**Mahmoud7111**
**omar-abass**
- GitHub: [@Mahmoud7111](https://github.com/Mahmoud7111)
- GitHub:omar-abass

---

## 📞 Contact & Support

If you have any questions or need support, please:
- Open an issue on GitHub
- Contact the repository owner

---

## ⭐ Show Your Support

If you found this project helpful, please give it a ⭐️!

---

## 📝 Changelog


### Version 1.0.0 (2026-01-31)
- Initial release
- Complete e-commerce functionality
- Admin, Seller, and Customer roles
- Product and order management
- Shopping cart system
- Revenue reporting
- File-based persistence

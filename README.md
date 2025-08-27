# DemoShoppingBackend

## Demo Video
[Watch the demo](./demo.mp4)

DemoShoppingBackend is the backend service for the Demo Shopping Application. It provides RESTful APIs for user authentication, product management, cart operations, and order processing.

## Technologies Used
- Java 17+
- Spring Boot
- Maven
- H2 Database (for development/testing)

## Project Structure
```
DemoShoppingBackend/
├── src/main/java/com/example/demo/         # Java source code
│   ├── controller/                         # REST controllers
│   ├── dto/                                # Data Transfer Objects
│   ├── model/                              # Entity models
│   ├── repository/                         # Spring Data repositories
│   ├── service/                            # Business logic
│   ├── OnlineShoppingSiteApplication.java  # Main application class
│   └── SecurityConfig.java                 # Security configuration
├── src/main/resources/                     # Resources
│   ├── application.properties              # App configuration
│   ├── data.sql                            # Initial data
│   ├── static/                             # Static files
│   └── templates/                          # Templates
├── src/test/java/com/example/demo/         # Test classes
├── pom.xml                                 # Maven build file
├── mvnw, mvnw.cmd                          # Maven wrapper
├── data/                                   # H2 database files
└── target/                                 # Build output
```

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven (or use the included Maven Wrapper)

### Running the Application
1. Open a terminal and navigate to the project directory:
   ```
   cd D:\DemoShoppingApplication\DemoShoppingBackend
   ```
2. Start the backend server:
   ```
   .\mvnw spring-boot:run
   ```
   The server will start on `http://localhost:8080` by default.

### Database
- The application uses an embedded H2 database for development.
- Initial data is loaded from `src/main/resources/data.sql`.

### Running Tests
To run unit and integration tests:
```
.\mvnw test
```

## API Endpoints
- `/api/products` - Product management
- `/api/cart` - Cart operations
- `/api/orders` - Order processing
- `/api/users` - User management

Refer to the controller classes for detailed endpoint documentation.

## Default Credentials
- Default users may be loaded via `data.sql` for testing purposes.

## Notes
- For production, configure a persistent database and update `application.properties` accordingly.
- Frontend is located in the `DemoShoppingFrontend` directory.

---

For any issues or contributions, please open an issue or submit a pull request.

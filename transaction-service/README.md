# Transaction Service

This project is a Spring Boot application designed for processing transactions in an idempotent manner. It provides a background transaction processing service that ensures each transaction is processed only once, even if the request is received multiple times.

## Features

- Idempotent transaction processing
- RESTful API for transaction management
- Spring Data JPA for database interactions
- Configurable application settings

## Project Structure

```
transaction-service
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── transactionservice
│   │   │               ├── TransactionServiceApplication.java
│   │   │               ├── config
│   │   │               │   └── AppConfig.java
│   │   │               ├── controller
│   │   │               │   └── TransactionController.java
│   │   │               ├── model
│   │   │               │   └── TransactionRecord.java
│   │   │               ├── service
│   │   │               │   └── TransactionProcessor.java
│   │   │               └── repository
│   │   │                   └── TransactionRepository.java
│   │   └── resources
│   │       ├── application.yml
│   │       └── logback-spring.xml
│   └── test
│       ├── java
│       │   └── com
│       │       └── example
│       │           └── transactionservice
│       │               └── TransactionServiceApplicationTests.java
├── pom.xml
└── README.md
```

## Setup Instructions

1. **Clone the repository:**
   ```
   git clone <repository-url>
   cd transaction-service
   ```

2. **Build the project:**
   ```
   mvn clean install
   ```

3. **Run the application:**
   ```
   mvn spring-boot:run
   ```

4. **Access the API:**
   The application will be available at `http://localhost:8080`. You can use tools like Postman or curl to interact with the API endpoints.

## Usage

- **Create a Transaction:**
  - Endpoint: `POST /transactions`
  - Body: `{ "amount": 100, "status": "PENDING" }`

- **Retrieve a Transaction:**
  - Endpoint: `GET /transactions/{id}`

## License

This project is licensed under the MIT License. See the LICENSE file for more details.
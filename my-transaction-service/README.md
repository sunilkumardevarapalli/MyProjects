# My Transaction Service

## Overview
My Transaction Service is a Spring Boot application designed for background transaction processing. It supports idempotent processing, duplicate detection, out-of-order request handling, and retry-safe failure handling. The service provides a clear transaction status lifecycle, processing summary, and basic operational visibility.

## Features
- **Idempotent Processing**: Ensures that repeated requests with the same transaction ID do not result in duplicate processing.
- **Duplicate Detection**: Automatically identifies and handles duplicate transactions.
- **Out-of-Order Request Handling**: Processes transactions that may arrive out of sequence.
- **Retry-Safe Failure Handling**: Safely retries failed transactions without risking data inconsistency.
- **Transaction Status Lifecycle**: Tracks the status of each transaction through various stages (e.g., PENDING, COMPLETED, FAILED, DUPLICATE).
- **Processing Summary**: Provides a summary of transaction processing results, including counts of processed, failed, and duplicate transactions.
- **Operational Visibility**: Includes metrics collection and logging for monitoring transaction processing.

## Getting Started

### Prerequisites
- Java 17
- Maven

### Installation
1. Clone the repository:
   ```
   git clone <repository-url>
   ```
2. Navigate to the project directory:
   ```
   cd my-transaction-service
   ```
3. Build the project using Maven:
   ```
   mvn clean install
   ```

### Running the Application
To run the application, use the following command:
```
mvn spring-boot:run
```

### API Endpoints
- **Submit Transaction**: `POST /transactions`
- **Get Transaction Status**: `GET /transactions/{transactionId}`

## Testing
Unit tests are provided to cover important edge cases. To run the tests, use:
```
mvn test
```

## Contributing
Contributions are welcome! Please open an issue or submit a pull request for any enhancements or bug fixes.

## License
This project is licensed under the MIT License. See the LICENSE file for details.
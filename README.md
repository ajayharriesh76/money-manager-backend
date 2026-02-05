# Money Manager Backend

Spring Boot REST API for Money Manager Application

## Tech Stack

- **Framework**: Spring Boot 3.2.0
- **Java Version**: 17
- **Database**: Oracle DB
- **Build Tool**: Maven
- **Architecture**: Controller-Service-Repository Pattern

## Project Structure

```
src/main/java/com/moneymanager/
├── controller/          # REST API Controllers
│   ├── TransactionController.java
│   └── AccountController.java
├── service/            # Business Logic
│   ├── TransactionService.java
│   └── AccountService.java
├── repository/         # Database Layer
│   ├── TransactionRepository.java
│   └── AccountRepository.java
├── model/             # Entity Models
│   ├── Transaction.java
│   └── Account.java
├── dto/               # Data Transfer Objects
│   ├── TransactionDTO.java
│   ├── TransactionRequestDTO.java
│   ├── DashboardSummaryDTO.java
│   └── AccountDTO.java
├── exception/         # Exception Handling
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   └── TransactionNotEditableException.java
├── config/            # Configuration Classes
│   └── CorsConfig.java
└── MoneyManagerApplication.java
```

## Features

### Transaction Management
- Create, Read, Update, Delete transactions
- Support for INCOME, EXPENSE, and TRANSFER types
- Category-wise tracking
- Office/Personal division
- Date and time tracking
- 12-hour edit window restriction
- Account balance updates

### Account Management
- Create and manage multiple accounts
- Support for CASH, BANK, CREDIT_CARD, WALLET types
- Automatic balance updates on transactions
- Account deletion

### Dashboard & Analytics
- Monthly, Weekly, Yearly summaries
- Category-wise expense breakdown
- Category-wise income breakdown
- Recent transaction history
- Total balance calculation

### Filtering & Search
- Filter by date range
- Filter by transaction type
- Filter by division (Office/Personal)
- Filter by category
- Get distinct categories

## API Endpoints

### Transactions

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/transactions` | Create new transaction |
| PUT | `/api/transactions/{id}` | Update transaction (within 12 hours) |
| DELETE | `/api/transactions/{id}` | Delete transaction (within 12 hours) |
| GET | `/api/transactions/{id}` | Get transaction by ID |
| GET | `/api/transactions` | Get all transactions |
| GET | `/api/transactions/date-range` | Get transactions by date range |
| GET | `/api/transactions/type/{type}` | Get transactions by type |
| GET | `/api/transactions/division/{division}` | Get transactions by division |
| GET | `/api/transactions/category/{category}` | Get transactions by category |
| GET | `/api/transactions/dashboard` | Get dashboard summary |
| GET | `/api/transactions/categories` | Get categories by type |

### Accounts

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/accounts` | Create new account |
| GET | `/api/accounts` | Get all accounts |
| GET | `/api/accounts/{accountName}` | Get account by name |
| DELETE | `/api/accounts/{id}` | Delete account |

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Oracle Database (11g or higher)

### Database Setup

1. Create a new Oracle database or use existing one
2. Run the SQL script:
```bash
sqlplus username/password@database < database-schema.sql
```

3. Update `application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:XE
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Running the Application

1. Clone the repository:
```bash
git clone <repository-url>
cd money-manager-backend
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

## Configuration

### Application Properties

```properties
# Server Configuration
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:XE
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.OracleDialect
```

## Business Rules

1. **Transaction Editability**: Transactions can only be edited or deleted within 12 hours of creation
2. **Account Balance**: Account balances are automatically updated when transactions are created, updated, or deleted
3. **Transfer Transactions**: Require both fromAccount and toAccount
4. **Income Transactions**: Can have toAccount (optional)
5. **Expense Transactions**: Can have fromAccount (optional)

## Error Handling

The application uses a global exception handler that returns appropriate HTTP status codes:

- `404 NOT FOUND`: Resource not found
- `400 BAD REQUEST`: Invalid request or transaction not editable
- `500 INTERNAL SERVER ERROR`: Server errors

## Deployment

### Building for Production

```bash
mvn clean package -DskipTests
```

This creates a JAR file in the `target/` directory.

### Running the JAR

```bash
java -jar target/money-manager-backend-1.0.0.jar
```

## Testing

Run tests using:
```bash
mvn test
```

## License

This project is developed as part of a hackathon assessment task.

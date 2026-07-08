# UnivaWealth — Savings & Investment Manager

> **Manage your savings, investments, and financial goals before graduation.**
A JavaFX desktop app for keeping track of savings accounts, investments, transactions, and personal savings goals. Built as my term project for **INF811D (Object Oriented Programming)**, MSc. Information Technology.

The idea is simple: most students and young professionals here juggle a savings account, maybe a Susu or fixed deposit, and a few personal goals (rent, laptop, school fees) — but rarely have one place that shows all of it together. UnivaWealth is a small step toward that: log in, see your net worth at a glance, and track where your money is going.

## ✨ Features

- **Accounts & authentication** :- Register and log in with a hashed password (bcrypt), a unique auto-generated account number, and a proper session so the app knows who's currently using it.
- **Dashboard** :- A quick overview of total savings, total investments, and combined net worth, plus a pie chart showing how your money is split across savings and the different investment types you hold.
- **Savings accounts** :- Track balances, interest rates, target amounts, and start/target dates for each savings account.
- **Investments** :-  Log investments with principal, interest rate, duration, and status, and let the app work out the expected balance at maturity.
- **Savings goals** :-  Set a target (e.g. "New Laptop — GHS 5,000 by December") and track progress as a percentage.
- **Transactions** :-  Every deposit, withdrawal, investment, login, and transfer gets logged with a category and timestamp, so there's a running history of activity.
- **All amounts in GHS** :-  Formatted with thousands separators (e.g. `GHS 12,500.00`), since this is built with the Ghanaian cedi in mind.

## 🛠 Technology Stack
| Technology | Purpose                       |
|------------|-------------------------------|
| Java 21+   | Programming language          |
| JavaFX     | Desktop UI                    |
| Maven      | Build & Dependency Management |
| SQLite     | Embedded database             |
| bcrypt     | Password encryption           |
| JDBC       | Database connectivity         |
| MaterialFX | Modern JavaFX controls        |
| CSS        | User interface styling        |

## 📂 Project Structure

```
src/main/java/com/dbank/uccunivawealth/
├── Launcher.java              # Entry point (works around JavaFX module quirks)
├── app/MainApp.java           # JavaFX Application — bootstraps the login screen
├── controller/                # One controller per FXML screen (login, dashboard,
│                              #   savings, investments, goals, transactions, etc.)
├── model/                     # Plain domain objects: User, Account, SavingsAccount,
│                              #   Investment, SavingsGoal, Transaction, ...
├── repo/                      # JDBC repositories — one per table, talking to SQLite
├── service/                   # AuthService, UserSession, AppData (in-memory cache
│                              #   of the current user's data), LoggerService
└── util/                      # Input validation, money formatting, misc UI helpers

src/main/resources/com/dbank/uccunivawealth/
├── *.fxml                     # Screen layouts (login, register, dashboard, savings,
│                              # investment, goals, transactions, main shell)
└── *.css                      # Styling

database/
├── univawealth.db             # SQLite database file (created/used at runtime)
└── scripts/
    ├── tables.sql             # Schema: Users, SavingsAccount, Investment,
    │                          #   Transactions, Goals, Categories, Notifications
    ├── indexes.sql            # Indexes on the foreign-key columns
    └── category-seed.sql      # Seed data for transaction categories
```

## 🏗 Architecture
The architecture roughly follows an MVC (Model-View-Controller): FXML defines the view, controllers handle UI logic, repositories handle persistence, and services (auth, session, app-wide data caching) sit in between.
The application follows the **MVC (Model-View-Controller)** design pattern.

### Controllers

Responsible for handling user interactions and coordinating between the UI and business logic.

Examples include:

- DashboardController
- LoginController
- InvestmentController
- GoalsController
- MainController

---

### Models

Represent the application's domain objects.

Examples include:

- User
- Investment
- SavingsAccount
- Account

---

### Repository Layer

Responsible for interacting with the SQLite database.

Examples:

- DatabaseManager
- InvestmentsRepository
- SavingsGoalsRepository

---

### Services

Contain reusable business logic.

Examples:

- Authentication
- Logging
- Application state management

---

### Utilities

Reusable helper classes for:

- Input validation
- Notifications
- UI utilities

---

## 💾 Database

The project uses **SQLite** as its database.

Database resources are located in:

```
database/
```

SQL scripts are provided under:

```
database/scripts
```

These scripts include:

- Database tables
- Indexes
- Category seed data

---

## Getting started

### Prerequisites

- Java JDK 21 or later
- Maven 3.9+
- IntelliJ IDEA (recommended)

### Clone the repository

```bash
git clone https://github.com/DannyBank/ucc-univawealth.git
```

---

### Navigate into the project

```bash
cd ucc-univawealth
```

---

### Build the project

```bash
mvn clean install
```

---


### Set up the database

The SQLite database is created at `database/univawealth.db`. If you're starting fresh, run the scripts in `database/scripts/` in this order against that file:

```bash
sqlite3 database/univawealth.db < database/scripts/tables.sql
sqlite3 database/scripts/category-seed.sql database/univawealth.db < database/scripts/category-seed.sql
sqlite3 database/univawealth.db < database/scripts/indexes.sql
```

(Or simply open the `univawealth.db` file in any SQLite client (eg, DB Browser for SQLite works well) and run the three scripts in order: tables, then seed data, then indexes.)

### Run the app

```bash
./mvnw clean javafx:run
```

On Windows:

```cmd
mvnw.cmd clean javafx:run
```

This launches the login screen first. New users can register from there — an account number is generated automatically and the password is hashed before it ever touches the database.

## A few notes on design decisions

- **Passwords are never stored in plain text.** `AuthService` hashes on registration with bcrypt and verifies against the hash on login — the raw password is discarded immediately.
- **`AppData` acts as a lightweight in-memory cache** of the logged-in user's savings, investments, and transactions, refreshed from the database when the dashboard needs current numbers, so we're not hitting SQLite on every UI redraw. This can be improved later with using Redis
- **Money is always in GHS**, formatted consistently through a single `UiUtils.formatMoney()` helper rather than scattering `String.format` calls across controllers.

## Known limitations

Due to limited timing given for this project, a few things are still rough in its edges:

- Deposit/withdrawal logic on `SavingsAccount` (`deposit()`, `withdraw()`, `applyMonthlyInterest()`) is stubbed out and needs to be wired up properly.
- Input validation (`InputValidator`) is currently a pass-through and should enforce real password/username rules.
- No automated compounding or interest-accrual job yet — interest is currently just used in balance projections, not applied over time.
- No Test coverage yet. Using JUnit 5 dependency, models and repositories would benefit from unit tests.
- Could eventually move from a local SQLite file to a networked database if this needed to support multiple devices per user.

## 📊 Project Modules

| Module | Description |
|---------|-------------|
| Authentication | User login and session handling |
| Dashboard | Financial overview |
| Savings | Savings account management |
| Investments | Investment management |
| Transactions | Transaction recording |
| Goals | Savings goal tracking |
| Logging | Centralized error logging |

---

## 🔮 Future Improvements

Some planned enhancements include:

- Charts and financial analytics
- Budget management
- Loan management
- Investment recommendations
- Email/SMS notifications
- Report generation
- Data export (Excel/PDF)
- Multi-user support
- Backup and restore
- Cloud synchronization

---

## 🤝 Contributing

Contributions are welcome.

1. Fork the repository.
2. Create a feature branch.
3. Commit your changes.
4. Push your branch.
5. Create a Pull Request.

---

## 👨‍💻 Author

**Daniel Bucknor-Ankrah**

MS/ITE/25/0020

MSc Information Technology

University of Cape Coast

---

## 📄 License

This project is developed for educational purposes as part of the MSc Information Technology programme at the University of Cape Coast.

---

## ⭐ Acknowledgements

- University of Cape Coast
- JavaFX
- MaterialFX
- SQLite
- Apache Maven
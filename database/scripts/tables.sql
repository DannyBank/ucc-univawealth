CREATE TABLE Users
(
    UserID INTEGER PRIMARY KEY AUTOINCREMENT,
    Username TEXT NOT NULL UNIQUE,
    PasswordHash TEXT NOT NULL,
    FullName TEXT NOT NULL,
    Email TEXT,
    Phone TEXT,
    DateCreated TEXT NOT NULL,
    LastLogin TEXT,
    IsActive INTEGER NOT NULL DEFAULT 1
);
CREATE TABLE SavingsAccount
(
    SavingsID INTEGER PRIMARY KEY AUTOINCREMENT,
    UserID INTEGER NOT NULL,
    AccountName TEXT NOT NULL,
    TargetAmount REAL NOT NULL,
    CurrentBalance REAL NOT NULL DEFAULT 0,
    InterestRate REAL DEFAULT 0,
    StartDate TEXT,
    TargetDate TEXT,
    Status TEXT DEFAULT 'ACTIVE',
    FOREIGN KEY(UserID)
        REFERENCES Users(UserID)
);
CREATE TABLE Investment
(
    InvestmentID INTEGER PRIMARY KEY AUTOINCREMENT,
    UserID INTEGER NOT NULL,
    InvestmentName TEXT,
    InvestmentType TEXT,
    Principal REAL,
    InterestRate REAL,
    DurationMonths INTEGER,
    StartDate TEXT,
    MaturityDate TEXT,
    ExpectedReturn REAL,
    Status TEXT DEFAULT 'ACTIVE',
    FOREIGN KEY(UserID)
        REFERENCES Users(UserID)
);
CREATE TABLE Categories
(
    CategoryID INTEGER PRIMARY KEY AUTOINCREMENT,
    CategoryName TEXT NOT NULL UNIQUE
);
CREATE TABLE Transactions
(
    TransactionID INTEGER PRIMARY KEY AUTOINCREMENT,
    UserID INTEGER NOT NULL,
    SavingsID INTEGER,
    InvestmentID INTEGER,
    TransactionType TEXT NOT NULL,
    Amount REAL NOT NULL,
    TransactionDate TEXT NOT NULL,
    CategoryID INTEGER,
    Notes TEXT,
    FOREIGN KEY(UserID)
        REFERENCES Users(UserID),
    FOREIGN KEY(SavingsID)
        REFERENCES SavingsAccount(SavingsID),
    FOREIGN KEY(InvestmentID)
        REFERENCES Investment(InvestmentID),
    FOREIGN KEY(CategoryID)
        REFERENCES Categories(CategoryID)
);
CREATE TABLE Goals
(
    GoalID INTEGER PRIMARY KEY AUTOINCREMENT,
    UserID INTEGER NOT NULL,
    SavingsID INTEGER,
    GoalName TEXT NOT NULL,
    TargetAmount REAL,
    CurrentAmount REAL,
    Deadline TEXT,
    Status TEXT DEFAULT 'IN_PROGRESS',
    FOREIGN KEY(UserID)
        REFERENCES Users(UserID),
    FOREIGN KEY(SavingsID)
        REFERENCES SavingsAccount(SavingsID)
);
CREATE TABLE Notifications
(
    NotificationID INTEGER PRIMARY KEY AUTOINCREMENT,
    UserID INTEGER,
    Title TEXT,
    Message TEXT,
    NotificationDate TEXT,
    IsRead INTEGER DEFAULT 0,
    FOREIGN KEY(UserID)
        REFERENCES Users(UserID)
);
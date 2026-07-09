CREATE TABLE Categories
(
    CategoryId INTEGER PRIMARY KEY AUTOINCREMENT,

    CategoryName TEXT NOT NULL UNIQUE
);

CREATE TABLE Goals
(
    GoalId INTEGER PRIMARY KEY AUTOINCREMENT,

    UserId INTEGER NOT NULL,

    SavingsId INTEGER,

    GoalName TEXT NOT NULL,

    TargetAmount REAL,

    CurrentAmount REAL,

    Deadline datetime,

    Status TEXT DEFAULT 'IN_PROGRESS',

    FOREIGN KEY(UserId)
        REFERENCES Users(UserId),

    FOREIGN KEY(SavingsId)
        REFERENCES SavingsAccount(SavingsId)
);

CREATE TABLE Investment
(
    InvestmentId INTEGER PRIMARY KEY AUTOINCREMENT,

    UserId INTEGER NOT NULL,

    InvestmentName TEXT,

    InvestmentType TEXT,

    Principal REAL,

    InterestRate REAL,

    DurationMonths INTEGER,

    StartDate datetime,

    MaturityDate datetime,

    ExpectedReturn REAL,

    Status TEXT DEFAULT 'ACTIVE',

    FOREIGN KEY(UserId)
        REFERENCES Users(UserId)
);

CREATE TABLE Notifications
(
    NotificationId INTEGER PRIMARY KEY AUTOINCREMENT,

    UserId INTEGER,

    Title TEXT,

    Message TEXT,

    NotificationDate datetime,

    IsRead INTEGER DEFAULT 0,

    FOREIGN KEY(UserId)
        REFERENCES Users(UserId)
);

CREATE TABLE "SavingsAccount" (
      "SavingsId"	INTEGER,

      "UserId"	INTEGER NOT NULL,

      "AccountNumber"	NUMERIC,

      "InitialBalance"	REAL,

      "InterestRate"	REAL DEFAULT 0,

      "TargetAmount"	REAL NOT NULL,

      "CurrentBalance"	REAL NOT NULL DEFAULT 0,

      "StartDate"	datetime,

      "TargetDate"	datetime,

      "Status"	TEXT DEFAULT 'ACTIVE',

      PRIMARY KEY("SavingsId" AUTOINCREMENT),

      FOREIGN KEY("UserId") REFERENCES "Users"("UserId")
);

CREATE TABLE Transactions
(
    TransactionId INTEGER PRIMARY KEY AUTOINCREMENT,

    UserId INTEGER NOT NULL,

    SavingsId INTEGER,

    InvestmentId INTEGER,

    TransactionType TEXT NOT NULL,

    Amount REAL NOT NULL,

    TransactionDate datetime NOT NULL,

    CategoryId INTEGER,

    Notes TEXT,

    FOREIGN KEY(UserId)
        REFERENCES Users(UserId),

    FOREIGN KEY(SavingsId)
        REFERENCES SavingsAccount(SavingsId),

    FOREIGN KEY(InvestmentId)
        REFERENCES Investment(InvestmentId),

    FOREIGN KEY(CategoryId)
        REFERENCES Categories(CategoryId)
);

CREATE TABLE "Users" (
     "UserId"	INTEGER,

     "Username"	TEXT NOT NULL UNIQUE,

     "AccountNumber"	NUMERIC,

     "PasswordHash"	TEXT NOT NULL,

     "FullName"	TEXT NOT NULL,

     "Email"	TEXT,

     "Phone"	TEXT,

     "DateCreated"	TEXT NOT NULL,

     "LastLogin"	TEXT,

     "IsActive"	INTEGER NOT NULL DEFAULT 1,

     PRIMARY KEY("UserId" AUTOINCREMENT)
);
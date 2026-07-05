-- create indexes to speed up data retrieval

CREATE INDEX IDX_Transaction_User
    ON Transactions(UserId);

CREATE INDEX IDX_Savings_User
    ON SavingsAccount(UserId);

CREATE INDEX IDX_Investment_User
    ON Investment(UserId);

CREATE INDEX IDX_Goal_User
    ON Goals(UserId);
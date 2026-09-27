# Al Baraka Bank — Console Banking App

A console banking application built with Java 17 + MariaDB. Manage clients and their
checking/savings accounts, record deposits, withdrawals and transfers, view history,
and run reports: Top 5 clients, monthly volume, suspect transactions and inactive accounts.

## Tech

| Tool | Version |
|------|---------|
| Java | 17 |
| Maven | 3.9 |
| MariaDB | 10/11 + `mariadb-java-client` 3.3.3 |
| Tests | JUnit 5 |

## Project structure

```
src/main/java/com/solubank/
  Main.java            entry point, checks DB then starts the menu
  models/              Account (abstract), CurrentAccount, SavingsAccount,
                       Client, Transaction
  enums/               TransactionType (DEPOSIT, WITHDRAW, TRANSFER)
  dao/                 ClientDAO, AccountDAO, TransactionDAO (JDBC, 1 query per call)
  services/            ClientService, AccountService, TransactionService, RapportService
  ui/                  MainMenu, ClientMenu, AccountMenu, TransactionMenu,
                       RapportMenu, Input
  dto/                 ClientTotal, MonthlyStats (query result holders)
  utils/               DBConnection, DbException, Validator, FormatUtil,
                       AccountNumberGenerator
src/main/resources/
  schema.sql           tables Customer / Account / Transaction + demo clients
docs/                 project docs
target/                 build output (ignored by git)
```

## Prerequisites

1. Java 17: `java -version`
2. MariaDB running on `localhost:3306` with user `root` / password `Admin1234@`
   (or override: `DB_URL`, `DB_USER`, `DB_PASSWORD` env vars)
3. Database + tables:
```bash
mariadb -uroot -p'Admin1234@' < src/main/resources/schema.sql
```

## Build & run

```bash
mvn package
java -jar target/albaraka-1.0.0-jar-with-dependencies.jar
```

Menu: `1` create client (+ required account), `2` create account,
`3/4/5` deposit/withdraw/transfer, `6` history, `7` analysis, `8` alerts, `0` quit.

Key rules: an account is mandatory — a client without an account is never saved.
Checking accounts allow overdraft down to `-overdraft_limit` (default 5000);
savings accounts cannot go negative.

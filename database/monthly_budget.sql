DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS income_source;
DROP TABLE IF EXISTS expense;

BEGIN TRANSACTION;

CREATE TABLE IF NOT EXISTS income_source (
    income_source_id serial PRIMARY KEY,
    name varchar(80) NOT NULL UNIQUE,
    amount decimal(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS expense (
    expense_id serial PRIMARY KEY,
    name varchar(80) NOT NULL UNIQUE,
    amount decimal(10,2) NOT NULL
);

--income-source
INSERT INTO income_source (name, amount)
VALUES ('Salary but the name is long for some reason', 5000.21);

INSERT INTO income_source (name, amount)
VALUES ('Other', 1000.45);

--expense
INSERT INTO expense (name, amount)
VALUES ('Rent', 1550.52);

INSERT INTO expense (name, amount)
VALUES ('Internet', 77.77);

INSERT INTO expense (name, amount)
VALUES ('Car Payment', 244.44);

COMMIT;
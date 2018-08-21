DROP TABLE IF EXISTS Client;

CREATE TABLE Client (
 clientId BIGINT AUTO_INCREMENT NOT NULL,
 name VARCHAR(50) NOT NULL,
 info VARCHAR(200) NOT NULL,
 isDeleted BOOLEAN NOT NULL DEFAULT FALSE,
 PRIMARY KEY(clientId)
);

CREATE UNIQUE INDEX idx_cl on Client(info);

INSERT INTO Client (clientId, name, info) VALUES (11111111111111111,'First', 'Info1');
INSERT INTO Client (clientId, name, info) VALUES (11111111111111112,'Second', 'Info2');
INSERT INTO Client (clientId, name, info) VALUES (11111111111111113,'Third', 'Info3');

DROP TABLE IF EXISTS Account;

CREATE TABLE Account (
 accountId LONG AUTO_INCREMENT NOT NULL,
 amount DECIMAL(20,2) DEFAULT 0,
 clientId BIGINT NOT NULL,
 isDeleted BOOLEAN NOT NULL DEFAULT FALSE,
 PRIMARY KEY (accountId),
 foreign key (clientId) references Client(clientId)
);

INSERT INTO Account (accountId, clientId, amount) VALUES (21111111111111111, 11111111111111111, 10000.0000);
INSERT INTO Account (accountId, clientId, amount) VALUES (21111111111111112,11111111111111111,200.0000);
INSERT INTO Account (accountId, clientId, amount) VALUES (21111111111111113,11111111111111112,30000.0000);
INSERT INTO Account (accountId, clientId, amount) VALUES (21111111111111114,11111111111111112,400.0000);
INSERT INTO Account (accountId, clientId, amount) VALUES (21111111111111115,11111111111111113,5000.0000);
INSERT INTO Account (accountId, clientId, amount) VALUES (21111111111111116,11111111111111113,600.0000);

DROP TABLE IF EXISTS ClientOperation;

CREATE TABLE ClientOperation (
 operationId LONG AUTO_INCREMENT NOT NULL,
 fromAccId LONG,
 toAccId LONG,
 sum DECIMAL(20,2),
 isDeleted BOOLEAN NOT NULL DEFAULT FALSE,
 PRIMARY KEY (operationId),
 foreign key (fromAccId) references Account(accountId),
 foreign key (toAccId) references Account(accountId)
);
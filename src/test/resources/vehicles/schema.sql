-- H2 compatible schema

CREATE TABLE Vehicle
(
    maker VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    type  VARCHAR(20)  NOT NULL,
    PRIMARY KEY (model)
);

CREATE TABLE Car
(
    vin             VARCHAR(17)    NOT NULL,
    model           VARCHAR(100)   NOT NULL,
    engine_capacity DECIMAL(4, 2)  NOT NULL,
    horsepower      INT            NOT NULL,
    price           DECIMAL(10, 2) NOT NULL,
    transmission    VARCHAR(20)    NOT NULL,
    PRIMARY KEY (vin),
    FOREIGN KEY (model) REFERENCES Vehicle (model)
);

CREATE TABLE Motorcycle
(
    vin             VARCHAR(17)    NOT NULL,
    model           VARCHAR(100)   NOT NULL,
    engine_capacity DECIMAL(4, 2)  NOT NULL,
    horsepower      INT            NOT NULL,
    price           DECIMAL(10, 2) NOT NULL,
    type            VARCHAR(20)    NOT NULL,
    PRIMARY KEY (vin),
    FOREIGN KEY (model) REFERENCES Vehicle (model)
);

CREATE TABLE Bicycle
(
    serial_number VARCHAR(20)    NOT NULL,
    model         VARCHAR(100)   NOT NULL,
    gear_count    INT            NOT NULL,
    price         DECIMAL(10, 2) NOT NULL,
    type          VARCHAR(20)    NOT NULL,
    PRIMARY KEY (serial_number),
    FOREIGN KEY (model) REFERENCES Vehicle (model)
);

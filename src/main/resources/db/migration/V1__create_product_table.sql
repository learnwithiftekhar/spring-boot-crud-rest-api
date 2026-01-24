CREATE TABLE products
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    price DOUBLE,
    category    VARCHAR(255),
    PRIMARY KEY (id)
);
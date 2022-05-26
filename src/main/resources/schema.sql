CREATE TABLE currency
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    code         VARCHAR(255),
    name         VARCHAR(255),
    rate_to_euro DECIMAL,
    CONSTRAINT pk_currency PRIMARY KEY (id)
);

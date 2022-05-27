CREATE TABLE IF NOT EXISTS currency
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    code         VARCHAR(255),
    name         VARCHAR(255),
    rate_to_euro DECIMAL,
    date         TIMESTAMP,
    CONSTRAINT pk_currency PRIMARY KEY (id),
    CONSTRAINT code_date_unique_key
        UNIQUE (code, date)
);

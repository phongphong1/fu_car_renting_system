CREATE TABLE renting_transaction (
     renting_transaction_id BIGSERIAL PRIMARY KEY,
     renting_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     total_price DECIMAL(12, 2),
     customer_id BIGINT NOT NULL,
     renting_status VARCHAR(50)
);

CREATE TABLE renting_detail (
    renting_transaction_id BIGINT NOT NULL,
    car_id BIGINT NOT NULL,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    price DECIMAL(12, 2),

    PRIMARY KEY (renting_transaction_id, car_id),

    CONSTRAINT fk_renting_detail_transaction FOREIGN KEY (renting_transaction_id) REFERENCES renting_transaction(renting_transaction_id)
);
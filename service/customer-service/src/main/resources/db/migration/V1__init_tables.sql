
CREATE TABLE customer (
      customer_id BIGSERIAL PRIMARY KEY,
      customer_name VARCHAR(255) NOT NULL,
      telephone VARCHAR(20),
      email VARCHAR(255) UNIQUE NOT NULL,
      customer_birthday DATE,
      customer_status VARCHAR(50),
      password VARCHAR(255) NOT NULL
);
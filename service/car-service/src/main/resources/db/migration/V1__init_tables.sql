
CREATE TABLE supplier (
      supplier_id BIGSERIAL PRIMARY KEY,
      supplier_name VARCHAR(255) NOT NULL,
      supplier_description TEXT,
      supplier_address VARCHAR(500)
);

CREATE TABLE manufacturer (
      manufacturer_id BIGSERIAL PRIMARY KEY,
      manufacturer_name VARCHAR(255) NOT NULL,
      description TEXT,
      manufacturer_country VARCHAR(100)
);

CREATE TABLE car_information (
     car_id BIGSERIAL PRIMARY KEY,
     car_name VARCHAR(255) NOT NULL,
     car_description TEXT,
     number_of_doors INT,
     seating_capacity INT,
     fuel_type VARCHAR(50),
     year INT,
     manufacturer_id BIGINT,
     supplier_id BIGINT,
     car_status VARCHAR(50),
     car_renting_price_per_day DECIMAL(12, 2),

     CONSTRAINT fk_car_manufacturer FOREIGN KEY (manufacturer_id) REFERENCES manufacturer(manufacturer_id),
     CONSTRAINT fk_car_supplier FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id)
);
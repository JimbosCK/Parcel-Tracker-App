CREATE TABLE shipping_address (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    phone VARCHAR(255),
    email VARCHAR(255),
    notes TEXT,
    street VARCHAR(255),
    city VARCHAR(255),
    zip_code VARCHAR(255),
    country VARCHAR(255)
);

CREATE TABLE parcel (
    id BIGSERIAL PRIMARY KEY,
    tracking_code VARCHAR(255) UNIQUE NOT NULL,
    current_location VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP WITHOUT TIME ZONE,
    eta_date DATE,
    shipping_address_id BIGINT REFERENCES shipping_address(id)
);

CREATE TABLE parcel_history (
    id BIGSERIAL PRIMARY KEY,
    parcel_id BIGINT NOT NULL REFERENCES parcel(id),
    time_stamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    location VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    comments TEXT
);
CREATE TABLE IF NOT EXISTS category (
    id integer NOT NULL PRIMARY KEY,
    description VARCHAR(255),
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS product (
    id integer NOT NULL PRIMARY KEY,
    description VARCHAR(255),
    name VARCHAR(255),
    available_quantity DOUBLE PRECISION NOT NULL,
    price NUMERIC(38,2),
    category_id INTEGER CONSTRAINT fk1asljdasjdlk REFERENCES category
);

CREATE SEQUENCE IF NOT EXISTS category_seq INCREMENT by 50;
CREATE SEQUENCE IF NOT EXISTS product_seq INCREMENT by 50;


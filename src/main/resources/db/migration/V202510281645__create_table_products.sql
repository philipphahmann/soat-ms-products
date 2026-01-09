-- create products category table
CREATE TYPE product_category AS ENUM ('SNACK', 'DRINK', 'DESSERT', 'SIDE_DISH');

-- create product table
CREATE TABLE products
(
    id          UUID PRIMARY KEY,
    sku         VARCHAR(16) UNIQUE,
    name        VARCHAR(255) NOT NULL,
    category    product_category NOT NULL,
    description VARCHAR(255) NOT NULL,
    price       NUMERIC(18, 4) NOT NULL CHECK (price > 0),
    active      BOOLEAN DEFAULT TRUE,
    image       VARCHAR(255) NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ
);

-- create indexes for optimized queries
CREATE INDEX idx_products_category ON products (category);
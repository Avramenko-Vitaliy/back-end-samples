CREATE TABLE users (
    id UUID CONSTRAINT users_pk PRIMARY KEY,
    first_name VARCHAR NOT NULL,
    last_name VARCHAR NOT NULL
);

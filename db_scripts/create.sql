CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE credentials (
    user_id INTEGER PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    password_hash CHAR(128) NOT NULL,
    salt CHAR(64) NOT NULL
);

CREATE TYPE country AS ENUM (
    'UNITED KINGDOM', 
    'USA', 
    'FRANCE', 
    'SPAIN', 
    'JAPAN'
);

CREATE TABLE location (
    id SERIAL PRIMARY KEY,
    x FLOAT NOT NULL, 
    y INT,
    z BIGINT,
    name VARCHAR(255)
);

CREATE TABLE coordinates (
    id SERIAL PRIMARY KEY,
    x FLOAT NOT NULL,
    y REAL NOT NULL
);

CREATE TABLE person (
    id SERIAL PRIMARY KEY,
    creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- автогенерация
    owner_id INT REFERENCES users(id) NOT NULL,
    name VARCHAR(255) NOT NULL,
    coordinates_id INT REFERENCES coordinates(id) ON DELETE CASCADE NOT NULL,
    height REAL,
    weight BIGINT,
    passport VARCHAR(24) NOT NULL,
    nationality country NOT NULL,
    location_id INT REFERENCES location(id) ON DELETE CASCADE NOT NULL
);


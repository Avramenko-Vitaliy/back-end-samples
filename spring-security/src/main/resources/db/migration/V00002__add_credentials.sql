DELETE FROM users;

ALTER TABLE users ADD password VARCHAR NOT NULL;
ALTER TABLE users ADD email VARCHAR NOT NULL;
CREATE UNIQUE INDEX users_email_uindex ON users (email);

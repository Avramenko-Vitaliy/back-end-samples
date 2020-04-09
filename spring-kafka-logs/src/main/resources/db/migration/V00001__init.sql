CREATE TABLE events (
    id INT CONSTRAINT events_pk PRIMARY KEY,
    api_key VARCHAR NOT NULL
);
CREATE UNIQUE INDEX events_api_key_uindex ON events (api_key);

INSERT INTO events(id, api_key) VALUES (1, 'REGISTER');

CREATE TABLE user_logs (
    id SERIAL CONSTRAINT user_logs_pk PRIMARY KEY,
    user_id UUID NOT NULL,
    email VARCHAR NOT NULL,
    first_name VARCHAR,
    last_name VARCHAR,
    event_id INT NOT NULL CONSTRAINT user_logs_events_id_fk REFERENCES events (id) ON UPDATE CASCADE ON DELETE CASCADE,
    creation_date TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL
);


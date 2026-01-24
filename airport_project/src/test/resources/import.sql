-- Countries
INSERT INTO countries (id, country_name) VALUES (1, 'Greece'), (2, 'Italy'), (3, 'Spain');

-- Cities
INSERT INTO cities (id, city_name, country_id) VALUES (1, 'Athens', 1), (2, 'Rome', 2), (3, 'Madrid', 3);

-- Airports
INSERT INTO airports (id, name, city_id, country_id, u3digitCode)
VALUES
  (1, 'Athens International Airport', 1, 1, 'ATH'),
  (2, 'Fiumicino Airport',             2, 2, 'FCO'),
  (3, 'Madrid Barajas Airport',         3, 3, 'MAD');

ALTER TABLE airports ALTER COLUMN id RESTART WITH 4;

-- Airlines
INSERT INTO airline (id, name, u2digitCode)
VALUES
  (1, 'Aegean Airlines', 'AA'),
  (2, 'Ryan Air', 'RR');

ALTER TABLE airline ALTER COLUMN id RESTART WITH 3;

-- Users
INSERT INTO users (
    id, username, first_name, last_name, email, phoneNumber, role, type
) VALUES (
    10, 'admin1', 'Alice', 'Admin', 'admin@aegean.com', '123456789', 'airline_admin', 'not_null'
);

INSERT INTO users (
    id, username, first_name, last_name, email, phoneNumber, role, type
) VALUES (
    20, 'passenger1', 'Bob', 'Passenger', 'bob@mail.com', '987654321', 'passenger', 'not_null'
);

INSERT INTO users (
    id, username, first_name, last_name, email, phoneNumber, role, type
) VALUES (
    30, 'sysAdmin1', 'Michael', 'Jordan', 'sys@admin.com', '987654321', 'system_admin', 'not_null'
);

-- Airline Administrators
INSERT INTO airline_administrator (
    user_id, airline_admin_id
) VALUES (
    10, 1
);

-- Passengers
INSERT INTO passengers (
    user_id, passport
) VALUES (
    20, 'P12345678'
);

-- Flights
INSERT INTO flights (
    id, flight_number, flight_uuid, airlineId,
    depAirportId, depTime, arrAirportId, arrTime, flight_status
)
VALUES (
    1,
    'A3-101',
    '123e4567-e89b-12d3-a456-426614174000',
    1,
    1,
    '2026-01-25 09:00:00',
    3,
    '2026-01-25 12:30:00',
    'SCHEDULED'
),(
      2,
      'A3-102',
      '1d2d5570-49a3-4f79-9a4f-653774c7cc64',
      1,
      3,
      '2026-01-25 09:00:00',
      1,
      '2026-01-25 12:30:00',
      'SCHEDULED'
  );

ALTER TABLE flights ALTER COLUMN id RESTART WITH 3;

-- Reservations
INSERT INTO reservations (
    id, reservation_uuid, created_at, user_id
)
VALUES (
    1,
    '550e8400-e29b-41d4-a716-446655440000',
    '2026-01-20 10:00:00',
    20
);

ALTER TABLE reservations ALTER COLUMN id RESTART WITH 2;

-- Tickets
INSERT INTO tickets (
    id, ticket_uuid, reservation_id, flight_id,
    firstName, lastName, passport
)
VALUES (
    1,
    'a1b2c3d4-e5f6-7890-abcd-000000000001',
    1,
    1,
    'Bob',
    'Passenger',
    'P12345678'
),
(
    2,
    'a1b2c3d4-e5f6-7890-abcd-000000000002',
    1,
    2,
    'Bob',
    'Passenger',
    'P12345678'
);

ALTER TABLE tickets ALTER COLUMN id RESTART WITH 3;

-- Aircraft
INSERT INTO aircrafts (id, aircraft_name, aircraft_capacity, aircraft_rows, aircraft_columns, airline_id)
VALUES
  (1, 'Airbus A320', 180, 30, 6, 1),
  (2, 'Boeing 737-800', 189, 31, 6, 1);

ALTER TABLE aircrafts ALTER COLUMN id RESTART WITH 3;

-- Flight seat layouts
INSERT INTO flight_seat_layouts (id, aircraft_id, flight_id)
VALUES
  (1, 1, 1);
INSERT INTO flight_seat_layouts (id, aircraft_id, flight_id)
VALUES
  (2, 1, 2);

ALTER TABLE flight_seat_layouts ALTER COLUMN id RESTART WITH 3;

-- Flight seat states
INSERT INTO flight_seat (id, seat_row, seat_column, state, flight_seat_layout_id)
VALUES
  (1, 1, 1, 'AVAILABLE', 1),
  (2, 1, 1, 'LOCKED', 2);

ALTER TABLE flight_seat ALTER COLUMN id RESTART WITH 3;

-- Airline Logos
INSERT INTO airline_logo (id, file_name, file_path, content, content_type)
VALUES
  (1, 'aegean_logo.png', '/mock/path/aegean_logo.png', X'89504E470D0A1A0A0000000D49484452', 'image/png'),
  (2, 'ryanair_logo.png', '/mock/path/ryanair_logo.png', X'89504E470D0A1A0A0000000D49484452', 'image/png');

UPDATE airline SET logo_id = 1 WHERE id = 1;
UPDATE airline SET logo_id = 2 WHERE id = 2;

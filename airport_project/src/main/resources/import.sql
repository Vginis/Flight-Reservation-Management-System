delete from Tickets;
delete from InFlightsReservations;
delete from OutFlightsReservations;
delete from Reservations;
delete from Flights;
delete from Airports;
delete from AccountManagement;

insert into AccountManagement(id, username, password, type, admin_id, name, u2digitCode, email, phoneNum, passport_id) values (3, 'admin', 'CAshaasjh!23', 'ADMINISTRATOR', 'IDK', null, null, null, null, null);
insert into AccountManagement(id, username, password, type, admin_id, name, u2digitCode, email, phoneNum, passport_id) values (4, 'aegean', 'CAshaasjh!23', 'AIRLINE', null, 'Aegean Airlines', 'A3', null, null, null);
insert into AccountManagement(id, username, password, type, admin_id, name, u2digitCode, email, phoneNum, passport_id) values (5, 'rayan', 'CAshaasjh!23', 'AIRLINE', null, 'Ryanair Ltd.', 'FR', null, null, null);
insert into AccountManagement(id, username, password, type, admin_id, name, u2digitCode, email, phoneNum, passport_id) values (6, 'passenger', 'CAshaasjh!23', 'PASSENGER', null, null, null, 'passenger@gmail.com', '1234567890', 'AK45FH5');
insert into AccountManagement(id, username, password, type, admin_id, name, u2digitCode, email, phoneNum, passport_id) values (9, 'transavia', 'CAshaasjh!23', 'AIRLINE', null, 'Transavia Airlines', 'TR', null, null, null);

insert into Airports(airportId, name, city, country, u3digitCode) values (1, 'Eleftherios Venizelos', 'Athens', 'Greece', 'ATH');
insert into Airports(airportId, name, city, country, u3digitCode) values (2, 'Fiumicino', 'Rome', 'Italy', 'FCO');
insert into Airports(airportId, name, city, country, u3digitCode) values (3, 'Venezuelo', 'Madrid', 'Spain', 'SPA');
insert into Airports(airportId, name, city, country, u3digitCode) values (4, 'Toussaint', 'Fontaine', 'Teyvat', 'TEY');


insert into Flights(id, FlightNo, airlineId, depAirportId, depTime, arrAirportId, arrTime, aircraftCapacity, aircraftType, TicketPrice, availableSeats) values (7, 'FR8438', 5, 1, '2023-07-19 21:00:00', 2, '2023-07-19 21:00:00', 200, 'Boeing-767', 100, 199);
insert into Flights(id, FlightNo, airlineId, depAirportId, depTime, arrAirportId, arrTime, aircraftCapacity, aircraftType, TicketPrice, availableSeats) values (8, 'A3651', 4, 2, '2023-07-19 21:00:00', 1, '2023-07-19 21:00:00', 178, 'Airbus-A320', 80, 177);


insert into Reservations(reservationId, passengerId, returnFlight, totalPrice) values (9, 6, false, 100);
insert into Reservations(reservationId, passengerId, returnFlight, totalPrice) values (10, 6, true, 180);

insert into OutFlightsReservations(flightId, reservationId) values (7, 9);
insert into OutFlightsReservations(flightId, reservationId) values (7, 10);

insert into InFlightsReservations(flightId, reservationId) values (8, 10);

insert into Tickets(ticketId, reservationId, luggageIncluded, weight, pieces, ticketPrice, seatNo, firstName, lastName, passportId, flightId) values (11, 9, false, 0, 0, 100, '1A', 'Bob', 'Wonder', 'CP152D45', 7);
insert into Tickets(ticketId, reservationId, luggageIncluded, weight, pieces, ticketPrice, seatNo, firstName, lastName, passportId, flightId) values (12, 10, false, 0, 0, 100, '1F', 'Art', 'Buster', 'AB42J34D', 7);
insert into Tickets(ticketId, reservationId, luggageIncluded, weight, pieces, ticketPrice, seatNo, firstName, lastName, passportId, flightId) values (13, 10, false, 0, 0, 80, '1A', 'Art', 'Buster', 'AB42J34D', 8);


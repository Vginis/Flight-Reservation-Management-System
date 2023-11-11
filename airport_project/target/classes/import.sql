-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database


insert into myentity (id, field, USERNAME, PASSWORD) values(4, 'field-1', 'Raiden', 'P@ss123');
insert into myentity (id, field, USERNAME, PASSWORD) values(5, 'field-2', 'Navia1', NULL);
insert into myentity (id, field, USERNAME, PASSWORD) values(6, 'field-3', 'Zhongli1', NULL);


-- alter sequence myentity_seq restart with 4;

-- Testing purposes

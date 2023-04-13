-- -----------------------------------------------------
-- `COMPANY BALANCE`
-- -----------------------------------------------------

INSERT INTO company_balance (id, balance) VALUES ('9fad775f-e3a6-41be-92f2-cbd72b7e24fb', '149.35');


-- -----------------------------------------------------
-- `TARIFFS`
-- -----------------------------------------------------

INSERT INTO tariffs (id, car_class, price_per_kilometer, driver_part_in_percent) VALUES ('4e77635c-b3ae-478a-b0e8-5dfd929a7f01', 'STANDARD', '9', '70');
INSERT INTO tariffs (id, car_class, price_per_kilometer, driver_part_in_percent) VALUES ('efb2f5de-c3e2-4056-a949-e84db49f7ba0', 'BUSINESS', '10', '75');
INSERT INTO tariffs (id, car_class, price_per_kilometer, driver_part_in_percent) VALUES ('e6923c19-b0f9-4e2b-a3a7-e67e305e0ffa', 'VIP', '11', '80');
INSERT INTO tariffs (id, car_class, price_per_kilometer, driver_part_in_percent) VALUES ('29b5d1af-fe6e-499f-a039-914295c63fe4', 'BUS_MINIVAN', '11', '85');


-- -----------------------------------------------------
-- `MANAGER`
-- -----------------------------------------------------

INSERT INTO person (id, email, name, surname, role, birth_date, phone, registration_date, enabled, password)
VALUES ('0ea0a09f-98d2-4491-8608-a68cb8001974', 'v_cheslav@ukr.net', 'Вячеслав', 'Гаврилюк', 'MANAGER', '1984-04-12', '+380961150084',
	   '2023-04-01', '1', '$2a$10$7i84Zd7hHbT4S7Kk7Dw.QOkKZ7NDJLu0VcU3OafcAoty1MIo6rvQe');

INSERT INTO manager (id) VALUES ('0ea0a09f-98d2-4491-8608-a68cb8001974');


-- -----------------------------------------------------
-- `PASSENGER`
-- -----------------------------------------------------

INSERT INTO person (id, email, name, surname, role, birth_date, phone, registration_date, enabled, password)
VALUES ('f263c4d9-b198-48c4-a97b-34cbb8387379', 'passenger1@gmail.com', 'passenger1Name', 'passenger1Surname', 'PASSENGER', '1994-07-12', '+380961150085',
	   '2023-04-01', '1', '$2a$10$7i84Zd7hHbT4S7Kk7Dw.QOkKZ7NDJLu0VcU3OafcAoty1MIo6rvQe');

INSERT INTO passenger (id, balance) VALUES ('f263c4d9-b198-48c4-a97b-34cbb8387379', '0');


INSERT INTO person (id, email, name, surname, role, birth_date, phone, registration_date, enabled, password)
VALUES ('df2d8a49-e706-4e2a-a9c1-357eab348737', 'passenger2@gmail.com', 'passenger2Name', 'passenger2Surname', 'PASSENGER', '2000-01-01', '+380961150086',
	   '2023-04-02', '1', '$2a$10$7i84Zd7hHbT4S7Kk7Dw.QOkKZ7NDJLu0VcU3OafcAoty1MIo6rvQe');

INSERT INTO passenger (id, balance) VALUES ('df2d8a49-e706-4e2a-a9c1-357eab348737', '0');

-- -----------------------------------------------------
-- `CAR`
-- -----------------------------------------------------

INSERT INTO car (id, brand, model, number, car_class) VALUES ('38a4b68b-dd79-48b2-b83f-67f9d5ae9693', 'Renault', 'Clio', 'AA1111AB', 'STANDARD');
INSERT INTO car (id, brand, model, number, car_class) VALUES ('435b7b65-e6d7-427a-9668-11ad2531e559', 'Honda', 'CR-V', 'AA2222AC', 'BUSINESS');
INSERT INTO car (id, brand, model, number, car_class) VALUES ('f96717f1-eecd-450c-817e-5ba2af4162de', 'Tesla', 'Model S', 'AA3333AM', 'VIP');
INSERT INTO car (id, brand, model, number, car_class) VALUES ('a9ac4033-8fde-4420-8f2b-9fbd814deadc', 'Volkswagen', 'T6 Transporter', 'AA4444AO', 'BUS_MINIVAN');


-- -----------------------------------------------------
-- `DRIVER`
-- -----------------------------------------------------

INSERT INTO person (id, email, name, surname, role, birth_date, phone, registration_date, enabled, password)
VALUES ('ab58f26c-d888-43b9-a98b-62609b091ee9', 'driver1@gmail.com', 'driver1Name', 'driver1Surname', 'DRIVER', '1975-07-12', '+380961150087',
	   '2023-04-01', '1', '$2a$10$7i84Zd7hHbT4S7Kk7Dw.QOkKZ7NDJLu0VcU3OafcAoty1MIo6rvQe');

INSERT INTO driver (id, balance, car_id) VALUES ('ab58f26c-d888-43b9-a98b-62609b091ee9', '50.79', '38a4b68b-dd79-48b2-b83f-67f9d5ae9693');


INSERT INTO person (id, email, name, surname, role, birth_date, phone, registration_date, enabled, password)
VALUES ('ce9b94e6-d7f1-400d-8666-d70fbb8c4a2e', 'driver2@gmail.com', 'driver2Name', 'driver2Surname', 'DRIVER', '1985-07-12', '+380961150088',
	   '2023-04-01', '1', '$2a$10$7i84Zd7hHbT4S7Kk7Dw.QOkKZ7NDJLu0VcU3OafcAoty1MIo6rvQe');

INSERT INTO driver (id, balance, car_id) VALUES ('ce9b94e6-d7f1-400d-8666-d70fbb8c4a2e', '37.40', '435b7b65-e6d7-427a-9668-11ad2531e559');


INSERT INTO person (id, email, name, surname, role, birth_date, phone, registration_date, enabled, password)
VALUES ('8e7c7eb6-3d6e-44e1-a96b-77b4552c90e0', 'driver3@gmail.com', 'driver3Name', 'driver3Surname', 'DRIVER', '1988-07-12', '+380961150089',
	   '2023-04-01', '1', '$2a$10$7i84Zd7hHbT4S7Kk7Dw.QOkKZ7NDJLu0VcU3OafcAoty1MIo6rvQe');

INSERT INTO driver (id, balance, car_id) VALUES ('8e7c7eb6-3d6e-44e1-a96b-77b4552c90e0', '344.19', 'f96717f1-eecd-450c-817e-5ba2af4162de');


INSERT INTO person (id, email, name, surname, role, birth_date, phone, registration_date, enabled, password)
VALUES ('6dc166cf-d80b-4e23-91ab-26d8288ac294', 'driver4@gmail.com', 'driver4Name', 'driver4Surname', 'DRIVER', '1995-07-12', '+380961150090',
	   '2023-04-01', '1', '$2a$10$7i84Zd7hHbT4S7Kk7Dw.QOkKZ7NDJLu0VcU3OafcAoty1MIo6rvQe');

INSERT INTO driver (id, balance, car_id) VALUES ('6dc166cf-d80b-4e23-91ab-26d8288ac294', '164.67', 'a9ac4033-8fde-4420-8f2b-9fbd814deadc');


-- -----------------------------------------------------
-- `ADDRESS`
-- -----------------------------------------------------

INSERT INTO address (id, address, latitude, longitude) VALUES
('6f854187-e21d-491f-b9ca-564f6795a90f', 'Ломоносова 34Б, вулиця Михайла Ломоносова, Київ, Україна', '50.3896648', '30.4776109'); --origin
INSERT INTO address (id, address, latitude, longitude) VALUES
('921b27f1-109d-44fd-b2ca-1d1248b8b51e', 'Велика Васильківська, 25, Київ, Україна', '50.4388224', '30.5165479'); --destination
INSERT INTO address (id, address, latitude, longitude) VALUES
('98ae0a8f-486e-4957-8f61-412116e9fbb6', 'вулиця Васильківська, 72, Київ, Україна', '50.3868805', '30.4804215'); --taxi

INSERT INTO address (id, address, latitude, longitude) VALUES
('39838fb6-0dd8-40e2-b0fd-9481cd9e6ea7', 'проспект Соборності, 12, Київ, Україна', '50.4408989', '30.619202'); --origin
INSERT INTO address (id, address, latitude, longitude) VALUES
('647d4460-69bb-4697-8fa8-51faf26dbd3e', 'вулиця Грушевського, 15, Васильків, Київська область, Україна', '50.178534', '30.3149996'); --destination
INSERT INTO address (id, address, latitude, longitude) VALUES
('4525deff-02a2-4ed2-8498-d920e78b1163', 'вулиця Тампере, 14, Київ, Україна', '50.44368799999999', '30.61360089999999'); --taxi

INSERT INTO address (id, address, latitude, longitude) VALUES
('bb0f6fbc-60d2-42c7-91b9-f991355b8758', 'вулиця Хрещатик, 26, Київ, Україна', '50.44892979999999', '30.5216049'); --origin
INSERT INTO address (id, address, latitude, longitude) VALUES
('a3c26711-2cf7-40bc-8fef-99da0f0c022a', 'вулиця Іоанна Павла II, 16, Київ, Україна', '50.4193921', '30.5358793'); --destination
INSERT INTO address (id, address, latitude, longitude) VALUES
('31d4d3f7-4bc4-470c-a62f-c8775db609a4', 'вулиця Володимирська, 23А, Київ, Україна', '50.4523995', '30.5163831'); --taxi

INSERT INTO address (id, address, latitude, longitude) VALUES
('9c5d2eba-5c40-4361-8022-409055310fb4', 'вулиця Енергетиків, 2, Київ, Україна', '50.4279144', '30.4898896'); --origin
INSERT INTO address (id, address, latitude, longitude) VALUES
('3bfec498-91a3-4162-b52d-fb8567dc3553', 'вулиця Миколи Закревського, 47, Київ, Україна', '50.5075601', '30.6177437'); --destination
INSERT INTO address (id, address, latitude, longitude) VALUES
('033b87e0-63fe-418c-bdc7-e8d124096516', 'вулиця Народна, 3, Київ, Україна', '50.4202854', '30.48596869999999'); --taxi

INSERT INTO address (id, address, latitude, longitude) VALUES
('24a4e305-14ae-4720-a348-59f0f05a6401', 'вулиця Дегтярівська, 25, Київ, Україна', '50.46052299999999', '30.4535007'); --origin
INSERT INTO address (id, address, latitude, longitude) VALUES
('20a31e0d-b395-4b99-ab40-a385f4cce7b1', 'проспект Перемоги 9а, проспект Перемоги, Київ, Україна', '50.4475735', '30.480442'); --destination
-- INSERT INTO address (id, address, latitude, longitude) VALUES
-- ('7ce00d37-b94b-4a3c-b124-301e5c77f5e2', 'вулиця Олександра Довженка, 4, Київ, Україна', '50.4572142', '30.4474439'); --taxi


-- -----------------------------------------------------
-- `TRIP`
-- -----------------------------------------------------

INSERT INTO trip (id, departure_date_time, origin_address, destination_address,
				  taxi_location_address, trip_status, payment_status, car_class, distance_in_meters, duration_in_seconds, time_to_taxi_arrival_in_seconds, 
				 price, passenger_id, driver_id)
VALUES ('75322a71-efed-4d4c-8fab-97105b107051', '2023-03-29', '6f854187-e21d-491f-b9ca-564f6795a90f', '921b27f1-109d-44fd-b2ca-1d1248b8b51e',
		'98ae0a8f-486e-4957-8f61-412116e9fbb6', 'COMPLETED', 'PAID', 'STANDARD', '8062', '688', '103',
	   '72.56', 'f263c4d9-b198-48c4-a97b-34cbb8387379', 'ab58f26c-d888-43b9-a98b-62609b091ee9');


INSERT INTO trip (id, departure_date_time, origin_address, destination_address,
				  taxi_location_address, trip_status, payment_status, car_class, distance_in_meters, duration_in_seconds, time_to_taxi_arrival_in_seconds, 
				 price, passenger_id, driver_id)
VALUES ('4f76a1f4-7bca-49ee-bdf1-c2df1cbff36d', '2023-03-30', '39838fb6-0dd8-40e2-b0fd-9481cd9e6ea7', '647d4460-69bb-4697-8fa8-51faf26dbd3e',
		'4525deff-02a2-4ed2-8498-d920e78b1163', 'DRIVING', 'PAID', 'VIP', '39113', '2366', '238',
	   '430.24', 'df2d8a49-e706-4e2a-a9c1-357eab348737', '8e7c7eb6-3d6e-44e1-a96b-77b4552c90e0');


INSERT INTO trip (id, departure_date_time, origin_address, destination_address,
				  taxi_location_address, trip_status, payment_status, car_class, distance_in_meters, duration_in_seconds, time_to_taxi_arrival_in_seconds, 
				 price, passenger_id, driver_id)
VALUES ('ade179df-e2aa-481e-9d33-db6c5073b965', '2023-03-30', 'bb0f6fbc-60d2-42c7-91b9-f991355b8758', 'a3c26711-2cf7-40bc-8fef-99da0f0c022a',
		'31d4d3f7-4bc4-470c-a62f-c8775db609a4', 'DRIVING', 'NOT_PAID', 'BUSINESS', '4987', '585', '232',
	   '49.87', 'df2d8a49-e706-4e2a-a9c1-357eab348737', 'ce9b94e6-d7f1-400d-8666-d70fbb8c4a2e');


INSERT INTO trip (id, departure_date_time, origin_address, destination_address,
				  taxi_location_address, trip_status, payment_status, car_class, distance_in_meters, duration_in_seconds, time_to_taxi_arrival_in_seconds, 
				 price, passenger_id, driver_id)
VALUES ('f57203ce-bf69-4e8d-b747-cb18b4298b94', '2023-04-01', '9c5d2eba-5c40-4361-8022-409055310fb4', '3bfec498-91a3-4162-b52d-fb8567dc3553',
		'033b87e0-63fe-418c-bdc7-e8d124096516', 'OFFERED', 'NOT_PAID', 'BUS_MINIVAN', '17612', '1505', '302',
	   '193.73', 'df2d8a49-e706-4e2a-a9c1-357eab348737', '6dc166cf-d80b-4e23-91ab-26d8288ac294');


INSERT INTO trip (id, departure_date_time, origin_address, destination_address,
				 trip_status, payment_status, car_class, distance_in_meters, duration_in_seconds, time_to_taxi_arrival_in_seconds, 
				 price, passenger_id)
VALUES ('40ed8777-9a01-433e-a0dc-a4ee031b6d17', '2023-04-15', '24a4e305-14ae-4720-a348-59f0f05a6401', '033b87e0-63fe-418c-bdc7-e8d124096516',
		'NEW', 'NOT_PAID', 'STANDARD', '5027', '642', '0',
	   '45.24', 'f263c4d9-b198-48c4-a97b-34cbb8387379');
		


INSERT INTO address (city, flat_number, house_number, street, zipcode) VALUES ('Lublin', '205', '1', 'Vetterów', '20-277');

INSERT INTO accounts (confirmation_token, account_status, date_registered, username, first_name, date_last_login, last_name, password, phone, reset_pass_token_exp, reset_pass_token, address_id) VALUES (NULL, 'VERIFIED', '2016-09-07 09:25:30.481','jaroslaw.bielec@pollub.edu.pl','Jarek', '2016-09-07 09:25:52.483', 'Bielec', '$shiro1$SHA-256$5$WeN9scyr77dkStAJk5s3pQ==$S7YhkIE5eUyJZ+VgYi2qHA+XDU2XY+zaehzkLJxKcSM=', NULL, NULL, NULL, 1);

INSERT INTO roles (name) VALUES ('Administrator');
INSERT INTO roles (name) VALUES ('Ankietowani');

INSERT INTO account_roles (username, role) VALUES ('jaroslaw.bielec@pollub.edu.pl', 'Administrator');

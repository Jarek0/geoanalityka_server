INSERT INTO addresses (id, city, flat_number, house_number, street, zipcode) VALUES (8, 'Lublin', '205', '1', 'Vetterów', '20-277');
INSERT INTO addresses (id, city, flat_number, house_number, street, zipcode) VALUES (9, 'Lublin', '205', '1', 'Vetterów', '20-277');

INSERT INTO companies (id, company_name, phone, tax_id, address_id) VALUES (8, 'Testowa', NULL, '3432432432', 9);

INSERT INTO accounts (account_type, id, confirmation_token, account_status, credits, date_registered, email_address, first_name, date_last_login, last_name, password, phone, queued_payment, reset_pass_token_exp, reset_pass_token, username, company_id, address_id) VALUES ('company', 2, NULL, 1, 500, '2016-09-07 09:25:30.481', 'qbns.spam@gmail.com', 'No', '2016-09-07 09:25:52.483', 'Name', '$shiro1$SHA-256$5$CaWENmyYL0yJHL4dT4Yzug==$wguom8QBra7+2SGWyKNDlOIvWNmg/l4qDRVITv5WqFI=', NULL, NULL, NULL, NULL, 'qbns.spam@gmail.com', 8, NULL);
INSERT INTO accounts (account_type, id, confirmation_token, account_status, credits, date_registered, email_address, first_name, date_last_login, last_name, password, phone, queued_payment, reset_pass_token_exp, reset_pass_token, username, company_id, address_id) VALUES ('natural_person', 1, NULL, 1, 500, '2016-09-07 09:21:47.165', 'jmalec@gis-expert.pl', 'Jakub', '2016-09-07 09:26:28.665', 'Malec', '$shiro1$SHA-256$5$WeN9scyr77dkStAJk5s3pQ==$S7YhkIE5eUyJZ+VgYi2qHA+XDU2XY+zaehzkLJxKcSM=', '857443983', NULL, NULL, NULL, 'jmalec@gis-expert.pl', NULL, 8);


INSERT INTO roles (id, name) VALUES (1, 'Administrator');
INSERT INTO roles (id, name) VALUES (2, 'PLAN_STANDARDOWY');
INSERT INTO roles (id, name) VALUES (3, 'PLAN_ZAAWANSOWANY');
INSERT INTO roles (id, name) VALUES (4, 'PLAN_DEDYKOWANY');
INSERT INTO roles (id, name) VALUES (5, 'PLAN_TESTOWY');

INSERT INTO account_roles (username, role) VALUES ('jmalec@gis-expert.pl', 'Administrator');
INSERT INTO account_roles (username, role) VALUES ('jmalec@gis-expert.pl', 'PLAN_DEDYKOWANY');
INSERT INTO account_roles (username, role) VALUES ('qbns.spam@gmail.com', 'PLAN_DEDYKOWANY');
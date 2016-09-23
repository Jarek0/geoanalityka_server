INSERT INTO addresses (city, flat_number, house_number, street, zipcode) VALUES ('Lublin', '205', '1', 'Vetterów', '20-277');
INSERT INTO addresses (city, flat_number, house_number, street, zipcode) VALUES ('Lublin', '205', '1', 'Vetterów', '20-277');

INSERT INTO companies (company_name, phone, tax_id, address_id) VALUES ('Testowa', NULL, '3432432432', 1);

INSERT INTO accounts (account_type, confirmation_token, account_status, credits, date_registered, email_address, first_name, date_last_login, last_name, password, phone, queued_payment, reset_pass_token_exp, reset_pass_token, username, company_id, address_id) VALUES ('company', NULL, 1, 500, '2016-09-07 09:25:30.481', 'qbns.spam@gmail.com', 'No', '2016-09-07 09:25:52.483', 'Name', '$shiro1$SHA-256$5$CaWENmyYL0yJHL4dT4Yzug==$wguom8QBra7+2SGWyKNDlOIvWNmg/l4qDRVITv5WqFI=', NULL, NULL, NULL, NULL, 'qbns.spam@gmail.com', 1, NULL);
INSERT INTO accounts (account_type, confirmation_token, account_status, credits, date_registered, email_address, first_name, date_last_login, last_name, password, phone, queued_payment, reset_pass_token_exp, reset_pass_token, username, company_id, address_id) VALUES ('natural_person', NULL, 1, 500, '2016-09-07 09:21:47.165', 'jmalec@gis-expert.pl', 'Jakub', '2016-09-07 09:26:28.665', 'Malec', '$shiro1$SHA-256$5$WeN9scyr77dkStAJk5s3pQ==$S7YhkIE5eUyJZ+VgYi2qHA+XDU2XY+zaehzkLJxKcSM=', '857443983', NULL, NULL, NULL, 'jmalec@gis-expert.pl', NULL, 2);


INSERT INTO roles (name) VALUES ('Administrator');
INSERT INTO roles (name) VALUES ('PLAN_STANDARDOWY');
INSERT INTO roles (name) VALUES ('PLAN_ZAAWANSOWANY');
INSERT INTO roles (name) VALUES ('PLAN_DEDYKOWANY');
INSERT INTO roles (name) VALUES ('PLAN_TESTOWY');

INSERT INTO account_roles (username, role) VALUES ('jmalec@gis-expert.pl', 'Administrator');
INSERT INTO account_roles (username, role) VALUES ('jmalec@gis-expert.pl', 'PLAN_DEDYKOWANY');


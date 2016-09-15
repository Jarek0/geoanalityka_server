INSERT INTO addresses (city, flat_number, house_number, street, zipcode) VALUES ('Lublin', '', '1', 'Vetterów', '20-277');
INSERT INTO companies (company_name, phone, tax_id, address_id) VALUES ('GIS-Expert Sp. z o.o.', '5435353534', '5435353534', 1);

INSERT INTO accounts (account_type, id, confirmation_token, account_status, credits, date_registered, email_address, first_name, date_last_login, last_name, password, phone, queued_payment, reset_pass_token_exp, reset_pass_token, username, company_id, address_id) VALUES ('company', 2, NULL, 1, 500, '2016-09-07 09:25:30.481', 'pkociuba@gis-expert.pl', 'No', '2016-09-07 09:25:52.483', 'Name', '$shiro1$SHA-256$5$CaWENmyYL0yJHL4dT4Yzug==$wguom8QBra7+2SGWyKNDlOIvWNmg/l4qDRVITv5WqFI=', NULL, NULL, NULL, NULL, 'pkociuba@gis-expert.pl', 1, NULL);


INSERT INTO roles (id, name) VALUES (1, 'Administrator');
INSERT INTO roles (id, name) VALUES (2, 'PLAN_STANDARDOWY');
INSERT INTO roles (id, name) VALUES (3, 'PLAN_ZAAWANSOWANY');
INSERT INTO roles (id, name) VALUES (4, 'PLAN_DEDYKOWANY');
INSERT INTO roles (id, name) VALUES (5, 'PLAN_TESTOWY');

INSERT INTO account_roles (username, role) VALUES ('pkociuba@gis-expert.pl', 'Administrator');


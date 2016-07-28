INSERT INTO addresses (city, flat_number, house_number, street, zipcode) VALUES ('Lublin', '', '1', 'Vetterów', '20-277');
INSERT INTO companies (company_name, phone, tax_id, address_id) VALUES ('GIS-Expert Sp. z o.o.', NULL, '5435353534', 1);
INSERT INTO addresses (city, flat_number, house_number, street, zipcode) VALUES ('Lublin', '', '1', 'Vetterów', '20-277');
INSERT INTO companies (company_name, phone, tax_id, address_id) VALUES ('GIS-Expert Sp. z o.o.', NULL, '5435353534', 2);
INSERT INTO addresses (city, flat_number, house_number, street, zipcode) VALUES ('Lublin', '', '1', 'Vetterów', '20-277');
INSERT INTO companies (company_name, phone, tax_id, address_id) VALUES ('GIS-Expert Sp. z o.o.', NULL, '5435353534', 3);
INSERT INTO addresses (city, flat_number, house_number, street, zipcode) VALUES ('Lublin', '', '1', 'Vetterów', '20-277');
INSERT INTO companies (company_name, phone, tax_id, address_id) VALUES ('GIS-Expert Sp. z o.o.', NULL, '5435353534', 4);
INSERT INTO addresses (city, flat_number, house_number, street, zipcode) VALUES ('Lublin', '', '1', 'Vetterów', '20-277');
INSERT INTO companies (company_name, phone, tax_id, address_id) VALUES ('GIS-Expert Sp. z o.o.', NULL, '5435353534', 5);

INSERT INTO accounts (confirmation_token, first_name, last_name, account_status, credits, date_registered, email_address, date_last_login, password, reset_pass_token_exp, reset_pass_token, username, company_id) VALUES (NULL, 'Jakub', 'Malec', 1, 5, '2016-06-14 10:23:01.13', 'jmalec@gis-expert.pl', NULL, '$shiro1$SHA-256$5$lrLKrXNW6wp5i6YnOcrDXA==$DhQ/QFPidtp3qJhlbgCnTes4k0E1Bl0g66v+0cS2Vto=', NULL, NULL, 'jmalec@gis-expert.pl', 1);
INSERT INTO accounts (confirmation_token, first_name, last_name, account_status, credits, date_registered, email_address, date_last_login, password, reset_pass_token_exp, reset_pass_token, username, company_id) VALUES (NULL, 'Zaawansowany', 'Plan',  1, 5, '2016-06-14 10:23:01.13', 'plan@zaawansowany.xx', NULL, '$shiro1$SHA-256$5$lrLKrXNW6wp5i6YnOcrDXA==$DhQ/QFPidtp3qJhlbgCnTes4k0E1Bl0g66v+0cS2Vto=', NULL, NULL, 'plan@zaawansowany.xx', 2);
INSERT INTO accounts (confirmation_token, first_name, last_name, account_status, credits, date_registered, email_address, date_last_login, password, reset_pass_token_exp, reset_pass_token, username, company_id) VALUES (NULL, 'Standardowy', 'Plan', 1, 5, '2016-06-14 10:23:01.13', 'plan@standardowy.xx', NULL, '$shiro1$SHA-256$5$lrLKrXNW6wp5i6YnOcrDXA==$DhQ/QFPidtp3qJhlbgCnTes4k0E1Bl0g66v+0cS2Vto=', NULL, NULL, 'plan@standardowy.xx', 3);
INSERT INTO accounts (confirmation_token, first_name, last_name, account_status, credits, date_registered, email_address, date_last_login, password, reset_pass_token_exp, reset_pass_token, username, company_id) VALUES (NULL, 'Dedykowany', 'Plan', 1, 5, '2016-06-14 10:23:01.13', 'plan@dedykowany.xx', NULL, '$shiro1$SHA-256$5$lrLKrXNW6wp5i6YnOcrDXA==$DhQ/QFPidtp3qJhlbgCnTes4k0E1Bl0g66v+0cS2Vto=', NULL, NULL, 'plan@dedykowany.xx', 4);
INSERT INTO accounts (confirmation_token, first_name, last_name, account_status, credits, date_registered, email_address, date_last_login, password, reset_pass_token_exp, reset_pass_token, username, company_id) VALUES (NULL, 'Testowy', 'Plan', 1, 5, '2016-06-14 10:23:01.13', 'plan@testowy.xx', NULL, '$shiro1$SHA-256$5$lrLKrXNW6wp5i6YnOcrDXA==$DhQ/QFPidtp3qJhlbgCnTes4k0E1Bl0g66v+0cS2Vto=', NULL, NULL, 'plan@testowy.xx', 5);

INSERT INTO roles (name) VALUES ('Administrator');
INSERT INTO roles (name) VALUES ('PLAN_STANDARDOWY');
INSERT INTO roles (name) VALUES ('PLAN_ZAAWANSOWANY');
INSERT INTO roles (name) VALUES ('PLAN_DEDYKOWANY');
INSERT INTO roles (name) VALUES ('PLAN_TESTOWY');

INSERT INTO account_roles (username, role) VALUES ('jmalec@gis-expert.pl', 'Administrator');
INSERT INTO account_roles (username, role) VALUES ('plan@zaawansowany.xx', 'PLAN_ZAAWANSOWANY');
INSERT INTO account_roles (username, role) VALUES ('plan@standardowy.xx', 'PLAN_STANDARDOWY');
INSERT INTO account_roles (username, role) VALUES ('plan@dedykowany.xx', 'PLAN_DEDYKOWANY');
INSERT INTO account_roles (username, role) VALUES ('plan@testowy.xx', 'PLAN_TESTOWY');
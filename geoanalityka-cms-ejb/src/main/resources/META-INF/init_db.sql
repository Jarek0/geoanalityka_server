INSERT INTO addresses (city, flat_number, house_number, street, zipcode) VALUES ('Lublin', '205', '1', 'Vetterów', '20-277');
INSERT INTO addresses (city, flat_number, house_number, street, zipcode) VALUES ('Lublin', '205', '1', 'Vetterów', '20-277');
INSERT INTO companies (company_name, phone, tax_id, address_id) VALUES ('Testowa', NULL, '3432432432', 1);

INSERT INTO addresses (city, flat_number, house_number, street, zipcode) VALUES ('Lublin', '', '1', 'Vetterów', '20-277');
INSERT INTO addresses (city, flat_number, house_number, street, zipcode) VALUES ('Poznań', '5', '8', 'Osiedle Lecha', '61-293');
INSERT INTO addresses (city, flat_number, house_number, street, zipcode) VALUES ('Warszawa', '', '1', 'Testowa', '00-000');
INSERT INTO addresses (city, flat_number, house_number, street, zipcode) VALUES ('Warszawa', '', '1', 'Testowa', '00-000');
INSERT INTO addresses (city, flat_number, house_number, street, zipcode) VALUES ('Warszawa', '', '1', 'Testowa', '00-000');
INSERT INTO addresses (city, flat_number, house_number, street, zipcode) VALUES ('Warszawa', '', '1', 'Testowa', '00-000');
INSERT INTO addresses (city, flat_number, house_number, street, zipcode) VALUES ('Warszawa', '', '1', 'Testowa', '00-000');
INSERT INTO addresses (city, flat_number, house_number, street, zipcode) VALUES ('Warszawa', '', '1', 'Testowa', '00-000');
INSERT INTO addresses (city, flat_number, house_number, street, zipcode) VALUES ('Warszawa', '', '1', 'Testowa', '00-000');
INSERT INTO addresses (city, flat_number, house_number, street, zipcode) VALUES ('Warszawa', '', '1', 'Testowa', '00-000');
INSERT INTO addresses (city, flat_number, house_number, street, zipcode) VALUES ('Warszawa', '', '1', 'Testowa', '00-000');
INSERT INTO addresses (city, flat_number, house_number, street, zipcode) VALUES ('Warszawa', '', '1', 'Testowa', '00-000');
INSERT INTO addresses (city, flat_number, house_number, street, zipcode) VALUES ('Warszawa', '', '1', 'Testowa', '00-000');
INSERT INTO addresses (city, flat_number, house_number, street, zipcode) VALUES ('Warszawa', '', '1', 'Testowa', '00-000');
INSERT INTO addresses (city, flat_number, house_number, street, zipcode) VALUES ('Warszawa', NULL, '1', 'Testowa', '00-000');

INSERT INTO companies (company_name, phone, tax_id, address_id) VALUES ('GIS-Expert sp. z o.o.', NULL, '712-31-70-358', 3);
INSERT INTO companies (company_name, phone, tax_id, address_id) VALUES ('Magdalena Biela', NULL, '111-222-33-55', 4);
INSERT INTO companies (company_name, phone, tax_id, address_id) VALUES ('Testowa Sp. z o.o.', NULL, '000-00-00-000', 5);
INSERT INTO companies (company_name, phone, tax_id, address_id) VALUES ('Testowa Sp. z o.o.', NULL, '000-00-00-000', 6);
INSERT INTO companies (company_name, phone, tax_id, address_id) VALUES ('Testowa Sp. z o.o.', NULL, '000-00-00-000', 7);
INSERT INTO companies (company_name, phone, tax_id, address_id) VALUES ('Testowa Sp. z o.o.', NULL, '000-00-00-000', 8);
INSERT INTO companies (company_name, phone, tax_id, address_id) VALUES ('Testowa Sp. z o.o.', NULL, '000-00-00-000', 9);
INSERT INTO companies (company_name, phone, tax_id, address_id) VALUES ('Testowa Sp. z o.o.', NULL, '000-00-00-000', 10);
INSERT INTO companies (company_name, phone, tax_id, address_id) VALUES ('Testowa Sp. z o.o.', NULL, '000-00-00-000', 11);
INSERT INTO companies (company_name, phone, tax_id, address_id) VALUES ('Testowa Sp. z o.o.', NULL, '000-00-00-000', 12);
INSERT INTO companies (company_name, phone, tax_id, address_id) VALUES ('Testowa Sp. z o.o.', NULL, '000-00-00-000', 13);
INSERT INTO companies (company_name, phone, tax_id, address_id) VALUES ('Testowa Sp. z o.o.', NULL, '000-00-00-000', 14);
INSERT INTO companies (company_name, phone, tax_id, address_id) VALUES ('Testowa Sp. z o.o.', NULL, '000-00-00-000', 15);
INSERT INTO companies (company_name, phone, tax_id, address_id) VALUES ('Testowa Sp. z o.o.', NULL, '000-00-00-000', 16);
INSERT INTO companies (company_name, phone, tax_id, address_id) VALUES ('Testowa Sp. z o.o.', '5000000000', '000-00-00-001', 17);



INSERT INTO accounts (account_type, confirmation_token, account_status, credits, date_registered, email_address, first_name, date_last_login, last_name, password, phone, queued_payment, reset_pass_token_exp, reset_pass_token, username, company_id, address_id) VALUES ('company', NULL, 1, 500, '2016-09-07 09:25:30.481', 'qbns.spam@gmail.com', 'No', '2016-09-07 09:25:52.483', 'Name', '$shiro1$SHA-256$5$CaWENmyYL0yJHL4dT4Yzug==$wguom8QBra7+2SGWyKNDlOIvWNmg/l4qDRVITv5WqFI=', NULL, NULL, NULL, NULL, 'qbns.spam@gmail.com', 1, NULL);
INSERT INTO accounts (account_type, confirmation_token, account_status, credits, date_registered, email_address, first_name, date_last_login, last_name, password, phone, queued_payment, reset_pass_token_exp, reset_pass_token, username, company_id, address_id) VALUES ('natural_person', NULL, 1, 500, '2016-09-07 09:21:47.165', 'jmalec@gis-expert.pl', 'Jakub', '2016-09-07 09:26:28.665', 'Malec', '$shiro1$SHA-256$5$WeN9scyr77dkStAJk5s3pQ==$S7YhkIE5eUyJZ+VgYi2qHA+XDU2XY+zaehzkLJxKcSM=', '857443983', NULL, NULL, NULL, 'jmalec@gis-expert.pl', NULL, 2);

INSERT INTO accounts (account_type, phone, confirmation_token, account_status, credits, date_registered, email_address, first_name, date_last_login, last_name, password, queued_payment, reset_pass_token_exp, reset_pass_token, username, company_id) VALUES ('company', NULL, NULL, 1, 30, '2016-06-14 10:23:01.13', 'lukasz.kwasniewski@wp.pl', 'Łukasz', NULL, 'Kwaśniewski', '$shiro1$SHA-256$5$lrLKrXNW6wp5i6YnOcrDXA==$DhQ/QFPidtp3qJhlbgCnTes4k0E1Bl0g66v+0cS2Vto=', NULL, NULL, NULL, 'lukasz.kwasniewski@wp.pl', 5);
INSERT INTO accounts (account_type, phone, confirmation_token, account_status, credits, date_registered, email_address, first_name, date_last_login, last_name, password, queued_payment, reset_pass_token_exp, reset_pass_token, username, company_id) VALUES ('company', NULL, NULL, 1, 30, '2016-06-14 10:23:01.13', 'sawicki.carolus@gmail.com', 'Karol', NULL, 'Sawicki', '$shiro1$SHA-256$5$lrLKrXNW6wp5i6YnOcrDXA==$DhQ/QFPidtp3qJhlbgCnTes4k0E1Bl0g66v+0cS2Vto=', NULL, NULL, NULL, 'sawicki.carolus@gmail.com', 6);
INSERT INTO accounts (account_type, phone, confirmation_token, account_status, credits, date_registered, email_address, first_name, date_last_login, last_name, password, queued_payment, reset_pass_token_exp, reset_pass_token, username, company_id) VALUES ('company', NULL, NULL, 1, 30, '2016-06-14 10:23:01.13', 'rafal.redzikowski@wp.pl', 'Rafał', NULL, 'Redzikowski', '$shiro1$SHA-256$5$lrLKrXNW6wp5i6YnOcrDXA==$DhQ/QFPidtp3qJhlbgCnTes4k0E1Bl0g66v+0cS2Vto=', NULL, NULL, NULL, 'rafal.redzikowski@wp.pl', 7);
INSERT INTO accounts (account_type, phone, confirmation_token, account_status, credits, date_registered, email_address, first_name, date_last_login, last_name, password, queued_payment, reset_pass_token_exp, reset_pass_token, username, company_id) VALUES ('company', NULL, NULL, 1, 30, '2016-06-14 10:23:01.13', 'agor@gor.strefa.pl', 'Agnieszka', NULL, 'Gornicka', '$shiro1$SHA-256$5$lrLKrXNW6wp5i6YnOcrDXA==$DhQ/QFPidtp3qJhlbgCnTes4k0E1Bl0g66v+0cS2Vto=', NULL, NULL, NULL, 'agor@gor.strefa.pl', 8);
INSERT INTO accounts (account_type, phone, confirmation_token, account_status, credits, date_registered, email_address, first_name, date_last_login, last_name, password, queued_payment, reset_pass_token_exp, reset_pass_token, username, company_id) VALUES ('company', NULL, NULL, 1, 30, '2016-06-14 10:23:01.13', 'mariusz.kamski@gmail.com', 'Mariusz', '2016-09-23 12:11:02.348', 'Kamski', '$shiro1$SHA-256$5$lrLKrXNW6wp5i6YnOcrDXA==$DhQ/QFPidtp3qJhlbgCnTes4k0E1Bl0g66v+0cS2Vto=', NULL, NULL, NULL, 'mariusz.kamski@gmail.com', 9);
INSERT INTO accounts (account_type, phone, confirmation_token, account_status, credits, date_registered, email_address, first_name, date_last_login, last_name, password, queued_payment, reset_pass_token_exp, reset_pass_token, username, company_id) VALUES ('company', NULL, NULL, 1, 30, '2016-06-14 10:23:01.13', 'jaroslaw.grabowski@gmail.com', 'Jarosław', NULL, 'Grabowski', '$shiro1$SHA-256$5$lrLKrXNW6wp5i6YnOcrDXA==$DhQ/QFPidtp3qJhlbgCnTes4k0E1Bl0g66v+0cS2Vto=', NULL, NULL, NULL, 'jaroslaw.grabowski@gmail.com', 10);
INSERT INTO accounts (account_type, phone, confirmation_token, account_status, credits, date_registered, email_address, first_name, date_last_login, last_name, password, queued_payment, reset_pass_token_exp, reset_pass_token, username, company_id) VALUES ('company', NULL, NULL, 1, 30, '2016-06-14 10:23:01.13', 'mirekcz@op.pl', 'Mirosław', NULL, 'Czywiński', '$shiro1$SHA-256$5$lrLKrXNW6wp5i6YnOcrDXA==$DhQ/QFPidtp3qJhlbgCnTes4k0E1Bl0g66v+0cS2Vto=', NULL, NULL, NULL, 'mirekcz@op.pl', 4);
INSERT INTO accounts (account_type, phone, confirmation_token, account_status, credits, date_registered, email_address, first_name, date_last_login, last_name, password, queued_payment, reset_pass_token_exp, reset_pass_token, username, company_id) VALUES ('company', NULL, NULL, 1, 30, '2016-06-14 10:23:01.13', 'marek.ozimek@gmail.com', 'Marek', '2016-09-20 14:46:24.856', 'Ozimek', '$shiro1$SHA-256$5$lrLKrXNW6wp5i6YnOcrDXA==$DhQ/QFPidtp3qJhlbgCnTes4k0E1Bl0g66v+0cS2Vto=', NULL, NULL, NULL, 'marek.ozimek@gmail.com', 16);
INSERT INTO accounts (account_type, phone, confirmation_token, account_status, credits, date_registered, email_address, first_name, date_last_login, last_name, password, queued_payment, reset_pass_token_exp, reset_pass_token, username, company_id) VALUES ('company', NULL, NULL, 1, 500, '2016-09-20 07:01:28.617', 'jimbojimers@gmail.com', 'Magda', '2016-09-22 10:55:54.68', 'Biela', '$shiro1$SHA-256$5$DhKKqe0gWjoMQdLipcvxuA==$lwmXboh+DI4AzwzoUwcE0aBV39hOKB/eh6bdip097K0=', NULL, NULL, NULL, 'jimbojimers@gmail.com', 3);
INSERT INTO accounts (account_type, phone, confirmation_token, account_status, credits, date_registered, email_address, first_name, date_last_login, last_name, password, queued_payment, reset_pass_token_exp, reset_pass_token, username, company_id) VALUES ('company', NULL, NULL, 1, 500, '2016-09-19 14:08:29.725', 'lprazmo@gis-expert.pl', 'Łukasz', '2016-09-21 12:30:54.759', 'Prażmo', '$shiro1$SHA-256$100000$$HyVDTDMLg+Jp0ZaYwvic8O0wbqMlY/FUVj6QUdU2IkI=', NULL, NULL, NULL, 'lprazmo@gis-expert.pl', 2);
INSERT INTO accounts (account_type, phone, confirmation_token, account_status, credits, date_registered, email_address, first_name, date_last_login, last_name, password, queued_payment, reset_pass_token_exp, reset_pass_token, username, company_id) VALUES ('company', NULL, NULL, 1, 30, '2016-06-14 10:23:01.13', 'adamczyk-pawel@wp.pl', 'Paweł', '2016-09-22 13:49:48.904', 'Adamczyk', '$shiro1$SHA-256$5$lrLKrXNW6wp5i6YnOcrDXA==$DhQ/QFPidtp3qJhlbgCnTes4k0E1Bl0g66v+0cS2Vto=', NULL, NULL, NULL, 'adamczyk-pawel@wp.pl', 11);


INSERT INTO roles (name) VALUES ('Administrator');
INSERT INTO roles (name) VALUES ('PLAN_STANDARDOWY');
INSERT INTO roles (name) VALUES ('PLAN_ZAAWANSOWANY');
INSERT INTO roles (name) VALUES ('PLAN_DEDYKOWANY');
INSERT INTO roles (name) VALUES ('PLAN_TESTOWY');

INSERT INTO account_roles (username, role) VALUES ('jmalec@gis-expert.pl', 'Administrator');
INSERT INTO account_roles (username, role) VALUES ('jmalec@gis-expert.pl', 'PLAN_DEDYKOWANY');
INSERT INTO account_roles (username, role) VALUES ('lprazmo@gis-expert.pl', 'PLAN_DEDYKOWANY');
INSERT INTO account_roles (username, role) VALUES ('jimbojimers@gmail.com', 'PLAN_DEDYKOWANY');
INSERT INTO account_roles (username, role) VALUES ('mirekcz@op.pl', 'PLAN_ZAAWANSOWANY');
INSERT INTO account_roles (username, role) VALUES ('marek.ozimek@gmail.com', 'PLAN_ZAAWANSOWANY');
INSERT INTO account_roles (username, role) VALUES ('lukasz.kwasniewski@wp.pl', 'PLAN_ZAAWANSOWANY');
INSERT INTO account_roles (username, role) VALUES ('sawicki.carolus@gmail.com', 'PLAN_ZAAWANSOWANY');
INSERT INTO account_roles (username, role) VALUES ('rafal.redzikowski@wp.pl', 'PLAN_ZAAWANSOWANY');
INSERT INTO account_roles (username, role) VALUES ('agor@gor.strefa.pl', 'PLAN_ZAAWANSOWANY');
INSERT INTO account_roles (username, role) VALUES ('mariusz.kamski@gmail.com', 'PLAN_ZAAWANSOWANY');
INSERT INTO account_roles (username, role) VALUES ('jaroslaw.grabowski@gmail.com', 'PLAN_ZAAWANSOWANY');

CREATE TABLE access_tokens (
    id bigint NOT NULL,
    expires timestamp without time zone NOT NULL,
    token character varying(36) NOT NULL
);

CREATE SEQUENCE access_tokens_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE access_tokens_id_seq OWNED BY access_tokens.id;

CREATE TABLE account_orders (
    account_id bigint,
    order_id bigint NOT NULL
);

CREATE TABLE account_roles (
    username character varying(80) NOT NULL,
    role character varying(255) NOT NULL
);

CREATE TABLE account_roles_aud (
    rev integer NOT NULL,
    username bigint NOT NULL,
    role bigint NOT NULL,
    revtype smallint
);

CREATE TABLE account_tokens (
    account_id bigint,
    token_id bigint NOT NULL
);

CREATE TABLE accounts (
    account_type character varying(31) NOT NULL,
    id bigint NOT NULL,
    confirmation_token character varying(36),
    account_status integer NOT NULL,
    credits double precision NOT NULL,
    date_registered timestamp without time zone NOT NULL,
    email_address character varying(80) NOT NULL,
    first_name character varying(50) NOT NULL,
    date_last_login timestamp without time zone,
    last_name character varying(30) NOT NULL,
    password character varying(102) NOT NULL,
    phone character varying(18),
    queued_payment double precision,
    reset_pass_token_exp timestamp without time zone,
    reset_pass_token character varying(64),
    username character varying(80) NOT NULL,
    company_id bigint,
    address_id bigint
);

CREATE TABLE accounts_aud (
    id bigint NOT NULL,
    rev integer NOT NULL,
    account_type character varying(31) NOT NULL,
    revtype smallint,
    address_id bigint,
    company_id bigint
);

CREATE SEQUENCE accounts_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE accounts_id_seq OWNED BY accounts.id;

CREATE TABLE addresses (
    id bigint NOT NULL,
    city character varying(50) NOT NULL,
    flat_number character varying(12),
    house_number character varying(12) NOT NULL,
    street character varying(100),
    zipcode character varying(12) NOT NULL
);

CREATE TABLE addresses_aud (
    id bigint NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    city character varying(50),
    flat_number character varying(12),
    house_number character varying(12),
    street character varying(100),
    zipcode character varying(12)
);

CREATE SEQUENCE addresses_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE addresses_id_seq OWNED BY addresses.id;

CREATE TABLE companies (
    id bigint NOT NULL,
    company_name character varying(100) NOT NULL,
    phone character varying(20),
    tax_id character varying(20) NOT NULL,
    address_id bigint
);

CREATE TABLE companies_aud (
    id bigint NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    company_name character varying(100),
    phone character varying(20),
    tax_id character varying(20),
    address_id bigint
);

CREATE SEQUENCE companies_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE companies_id_seq OWNED BY companies.id;

CREATE TABLE demographic_analyses (
    analysis_type character varying(31) NOT NULL,
    id bigint NOT NULL,
    date_finished timestamp without time zone,
    date_started timestamp without time zone,
    hash character varying(36) NOT NULL,
    name character varying(255),
    status integer,
    areatype integer NOT NULL,
    geojsonarea text,
    inhabitedpremises integer,
    location bytea NOT NULL,
    locationdisplayname character varying(255),
    radius integer,
    traveltime integer,
    traveltype integer,
    age_range character varying(7),
    analysis_data bytea,
    total_population integer,
    account_id bigint,
    CONSTRAINT demographic_analyses_traveltime_check CHECK (((traveltime >= 1) AND (traveltime <= 60)))
);

CREATE SEQUENCE demographic_analyses_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE demographic_analyses_id_seq OWNED BY demographic_analyses.id;

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE invoices (
    id bigint NOT NULL,
    date_created date,
    data bytea,
    mime_type character varying(255),
    original boolean,
    serial_id character varying(30)
);

CREATE SEQUENCE invoices_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE invoices_id_seq OWNED BY invoices.id;


CREATE TABLE login_attempts (
    id bigint NOT NULL,
    date timestamp without time zone NOT NULL,
    successful boolean NOT NULL,
    account_id bigint
);

CREATE SEQUENCE login_attempts_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE login_attempts_id_seq OWNED BY login_attempts.id;

CREATE TABLE order_invoices (
    order_id bigint,
    invoice_id bigint NOT NULL
);

CREATE TABLE orders (
    id bigint NOT NULL,
    amount integer,
    date timestamp without time zone,
    order_hash character varying(36),
    order_type integer NOT NULL,
    payuorderid character varying(32),
    payupaymenturl character varying(1000),
    status integer
);

CREATE SEQUENCE orders_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE orders_id_seq OWNED BY orders.id;

CREATE TABLE revinfo (
    rev integer NOT NULL,
    revtstmp bigint
);

CREATE TABLE roles (
    id bigint NOT NULL,
    name character varying(255)
);

CREATE TABLE roles_aud (
    id bigint NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    name character varying(255)
);

CREATE SEQUENCE roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE roles_id_seq OWNED BY roles.id;


ALTER TABLE ONLY access_tokens ALTER COLUMN id SET DEFAULT nextval('access_tokens_id_seq'::regclass);
ALTER TABLE ONLY accounts ALTER COLUMN id SET DEFAULT nextval('accounts_id_seq'::regclass);
ALTER TABLE ONLY addresses ALTER COLUMN id SET DEFAULT nextval('addresses_id_seq'::regclass);
ALTER TABLE ONLY companies ALTER COLUMN id SET DEFAULT nextval('companies_id_seq'::regclass);
ALTER TABLE ONLY demographic_analyses ALTER COLUMN id SET DEFAULT nextval('demographic_analyses_id_seq'::regclass);
ALTER TABLE ONLY invoices ALTER COLUMN id SET DEFAULT nextval('invoices_id_seq'::regclass);
ALTER TABLE ONLY login_attempts ALTER COLUMN id SET DEFAULT nextval('login_attempts_id_seq'::regclass);
ALTER TABLE ONLY orders ALTER COLUMN id SET DEFAULT nextval('orders_id_seq'::regclass);
ALTER TABLE ONLY roles ALTER COLUMN id SET DEFAULT nextval('roles_id_seq'::regclass);

ALTER TABLE ONLY access_tokens
    ADD CONSTRAINT access_tokens_pkey PRIMARY KEY (id);
ALTER TABLE ONLY account_orders
    ADD CONSTRAINT account_orders_pkey PRIMARY KEY (order_id);
ALTER TABLE ONLY account_roles_aud
    ADD CONSTRAINT account_roles_aud_pkey PRIMARY KEY (rev, username, role);
ALTER TABLE ONLY account_roles
    ADD CONSTRAINT account_roles_pkey PRIMARY KEY (username, role);
ALTER TABLE ONLY account_tokens
    ADD CONSTRAINT account_tokens_pkey PRIMARY KEY (token_id);
ALTER TABLE ONLY accounts_aud
    ADD CONSTRAINT accounts_aud_pkey PRIMARY KEY (id, rev);
ALTER TABLE ONLY accounts
    ADD CONSTRAINT accounts_pkey PRIMARY KEY (id);
ALTER TABLE ONLY addresses_aud
    ADD CONSTRAINT addresses_aud_pkey PRIMARY KEY (id, rev);
ALTER TABLE ONLY addresses
    ADD CONSTRAINT addresses_pkey PRIMARY KEY (id);
ALTER TABLE ONLY companies_aud
    ADD CONSTRAINT companies_aud_pkey PRIMARY KEY (id, rev);
ALTER TABLE ONLY companies
    ADD CONSTRAINT companies_pkey PRIMARY KEY (id);
ALTER TABLE ONLY demographic_analyses
    ADD CONSTRAINT demographic_analyses_pkey PRIMARY KEY (id);
ALTER TABLE ONLY demographic_analyses
    ADD CONSTRAINT demographic_analysis_hash_index UNIQUE (hash);
ALTER TABLE ONLY invoices
    ADD CONSTRAINT invoices_pkey PRIMARY KEY (id);
ALTER TABLE ONLY login_attempts
    ADD CONSTRAINT login_attempts_pkey PRIMARY KEY (id);
ALTER TABLE ONLY orders
    ADD CONSTRAINT order_hash_index UNIQUE (order_hash);
ALTER TABLE ONLY order_invoices
    ADD CONSTRAINT order_invoices_pkey PRIMARY KEY (invoice_id);
ALTER TABLE ONLY orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);
ALTER TABLE ONLY revinfo
    ADD CONSTRAINT revinfo_pkey PRIMARY KEY (rev);
ALTER TABLE ONLY roles_aud
    ADD CONSTRAINT roles_aud_pkey PRIMARY KEY (id, rev);
ALTER TABLE ONLY roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);
ALTER TABLE ONLY access_tokens
    ADD CONSTRAINT token_index UNIQUE (token);
ALTER TABLE ONLY roles
    ADD CONSTRAINT uk_ofx66keruapi6vyqpv6f2or37 UNIQUE (name);
ALTER TABLE ONLY accounts
    ADD CONSTRAINT username_index UNIQUE (username);

CREATE INDEX company_name_index ON companies USING btree (company_name);
CREATE INDEX company_name_taxid_index ON companies USING btree (company_name, tax_id);
CREATE INDEX company_taxid_index ON companies USING btree (tax_id);
CREATE INDEX demographic_analysis_account_index ON demographic_analyses USING btree (account_id);
CREATE INDEX invoices_date_index ON invoices USING btree (date_created);
CREATE INDEX login_attempts_date_account_index ON login_attempts USING btree (date, account_id);
CREATE INDEX role_username_index ON account_roles USING btree (username);

ALTER TABLE ONLY login_attempts
    ADD CONSTRAINT fk1sof2im7gh0k5hg712fauwb0e FOREIGN KEY (account_id) REFERENCES accounts(id);
ALTER TABLE ONLY roles_aud
    ADD CONSTRAINT fk3udarg0q4wa62rnnfcf6b46a2 FOREIGN KEY (rev) REFERENCES revinfo(rev);
ALTER TABLE ONLY accounts_aud
    ADD CONSTRAINT fk4fc2osoc9ougcyxdyxohsgc1m FOREIGN KEY (rev) REFERENCES revinfo(rev);
ALTER TABLE ONLY addresses_aud
    ADD CONSTRAINT fk50mnuowbhm0kdar74cu56p098 FOREIGN KEY (rev) REFERENCES revinfo(rev);
ALTER TABLE ONLY companies_aud
    ADD CONSTRAINT fk82wp3cbuh86h9cxmevg50gsly FOREIGN KEY (rev) REFERENCES revinfo(rev);
ALTER TABLE ONLY companies
    ADD CONSTRAINT fk8w70yf6urddd0ky7ev90okenf FOREIGN KEY (address_id) REFERENCES addresses(id);
ALTER TABLE ONLY account_orders
    ADD CONSTRAINT fkbusyjqn8s1roou8lkr9xn7qxe FOREIGN KEY (account_id) REFERENCES accounts(id);
ALTER TABLE ONLY accounts
    ADD CONSTRAINT fkgy644msmh1e8swyerk89yun6g FOREIGN KEY (address_id) REFERENCES addresses(id);
ALTER TABLE ONLY account_roles_aud
    ADD CONSTRAINT fkktp9bcpmbq5vyumoa3gkrdbb2 FOREIGN KEY (rev) REFERENCES revinfo(rev);
ALTER TABLE ONLY order_invoices
    ADD CONSTRAINT fkp378jl7fuclx1xyh7mnjgoja6 FOREIGN KEY (order_id) REFERENCES orders(id);
ALTER TABLE ONLY account_orders
    ADD CONSTRAINT fkp8io4qho7pu9irqjx6a8yydxw FOREIGN KEY (order_id) REFERENCES orders(id);
ALTER TABLE ONLY accounts
    ADD CONSTRAINT fkpduos3mohw3vep07cj5fwplb7 FOREIGN KEY (company_id) REFERENCES companies(id);
ALTER TABLE ONLY account_tokens
    ADD CONSTRAINT fkpf3hcsyxfyscjcw2khh2a69m8 FOREIGN KEY (account_id) REFERENCES accounts(id);
ALTER TABLE ONLY account_tokens
    ADD CONSTRAINT fks1boy9623j5nidol89g7fgqw5 FOREIGN KEY (token_id) REFERENCES access_tokens(id);
ALTER TABLE ONLY demographic_analyses
    ADD CONSTRAINT fkswj8h4hotytcggu2o1hn7vfeo FOREIGN KEY (account_id) REFERENCES accounts(id);
ALTER TABLE ONLY order_invoices
    ADD CONSTRAINT fkvsgb5x50hsg84ev6x8pulcli FOREIGN KEY (invoice_id) REFERENCES invoices(id);
    
    
-- CREATE SAMPLE USERS


DO $$
BEGIN

---------------------------
-- Create the tenants   --
---------------------------
IF NOT EXISTS (
  SELECT FROM pg_catalog.pg_roles
  WHERE  rolname = 'tenant_a') THEN

    CREATE ROLE tenant_a LOGIN PASSWORD 'tenant_a';

END IF;

IF NOT EXISTS (
  SELECT FROM pg_catalog.pg_roles
  WHERE  rolname = 'tenant_b') THEN

    CREATE ROLE tenant_b LOGIN PASSWORD 'tenant_b';

END IF;

---------------------------
-- Create the Schema     --
---------------------------
CREATE SCHEMA IF NOT EXISTS multitenant;


----------------------------
-- Create Sequences       --
----------------------------
CREATE SEQUENCE IF NOT EXISTS multitenant.customer_seq
    start 38187
    increment 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS multitenant.address_seq
    start 38187
    increment 1
    NO MAXVALUE
    CACHE 1;

----------------------------
-- Create the Tables      --
----------------------------
CREATE TABLE IF NOT EXISTS multitenant.customer
(
	customer_id integer default nextval('multitenant.customer_seq'),
	first_name VARCHAR(255) NOT NULL,
	last_name VARCHAR(255) NOT NULL,
	tenant_name VARCHAR(255) NOT NULL,
    CONSTRAINT customer_pkey
        PRIMARY KEY (customer_id)

);

CREATE TABLE IF NOT EXISTS multitenant.address
(
	address_id integer default nextval('multitenant.address_seq'),
	name VARCHAR(255) NOT NULL,
	street VARCHAR(255) NULL,
	postalcode VARCHAR(255) NULL,
	city VARCHAR(255) NULL,
	country VARCHAR(255) NULL,
	tenant_name VARCHAR(255) NOT NULL,
    CONSTRAINT address_pkey
        PRIMARY KEY (address_id)

);

CREATE TABLE IF NOT EXISTS multitenant.customer_address
(
	customer_id integer NOT NULL,
	address_id integer NOT NULL,
	tenant_name VARCHAR(255) NOT NULL,
	CONSTRAINT fk_customer_address_customer
		FOREIGN KEY(customer_id) 
		REFERENCES multitenant.customer(customer_id),
	CONSTRAINT fk_customer_address_address
		FOREIGN KEY(address_id) 
		REFERENCES multitenant.address(address_id)
);

---------------------------
-- Enable RLS            --
---------------------------
ALTER TABLE multitenant.customer 
    ENABLE ROW LEVEL SECURITY;

ALTER TABLE multitenant.address 
    ENABLE ROW LEVEL SECURITY;

ALTER TABLE multitenant.customer_address 
    ENABLE ROW LEVEL SECURITY;

---------------------------
-- Create the RLS Policy --
---------------------------
DROP POLICY IF EXISTS tenant_customer_isolation_policy ON multitenant.customer;
DROP POLICY IF EXISTS tenant_address_isolation_policy ON multitenant.address;
DROP POLICY IF EXISTS tenant_customer_address_isolation_policy ON multitenant.customer_address;

CREATE POLICY tenant_customer_isolation_policy ON multitenant.customer
    USING (tenant_name = current_user);

CREATE POLICY tenant_address_isolation_policy ON multitenant.address
    USING (tenant_name = current_user);

CREATE POLICY tenant_customer_address_isolation_policy ON multitenant.customer_address
    USING (tenant_name = current_user);

--------------------------------
-- Grant Access to the Schema --
--------------------------------
GRANT USAGE ON SCHEMA multitenant TO tenant_a;
GRANT USAGE ON SCHEMA multitenant TO tenant_b;

-------------------------------------------------------
-- Grant Access to multitenant.customer for Tenant A --
-------------------------------------------------------
GRANT ALL ON SEQUENCE multitenant.customer_seq TO tenant_a;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE multitenant.customer TO tenant_a;

GRANT ALL ON SEQUENCE multitenant.address_seq TO tenant_a;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE multitenant.address TO tenant_a;

GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE multitenant.customer_address TO tenant_a;

-------------------------------------------------------
-- Grant Access to multitenant.customer for Tenant B --
-------------------------------------------------------
GRANT ALL ON SEQUENCE multitenant.customer_seq TO tenant_b;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE multitenant.customer TO tenant_b;

GRANT ALL ON SEQUENCE multitenant.address_seq TO tenant_b;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE multitenant.address TO tenant_b;

GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE multitenant.customer_address TO tenant_b;

END;
$$;
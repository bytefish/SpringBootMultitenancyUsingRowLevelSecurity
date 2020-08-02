DO $$
BEGIN

---------------------------
-- Create the Schema     --
---------------------------
IF NOT EXISTS (SELECT 1 FROM information_schema.schemata WHERE schema_name = 'sample') THEN

    CREATE SCHEMA sample;

END IF;

---------------------------
-- Create the Table      --
---------------------------
IF NOT EXISTS (
	SELECT 1 
	FROM information_schema.tables 
	WHERE  table_schema = 'sample' 
	AND table_name = 'customer'
) THEN

CREATE TABLE sample.customer
(
	customer_id SERIAL PRIMARY KEY,
	first_name VARCHAR(255) NOT NULL,
	last_name VARCHAR(255) NOT NULL,
	tenant_name VARCHAR(255) NOT NULL
);

END IF;

IF NOT EXISTS (
	SELECT 1 
	FROM information_schema.tables 
	WHERE  table_schema = 'sample' 
	AND table_name = 'address'
) THEN

CREATE TABLE sample.address
(
	address_id SERIAL PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	street VARCHAR(255) NULL,
	postalcode VARCHAR(255) NULL,
	city VARCHAR(255) NULL,
	country VARCHAR(255) NULL,
	tenant_name VARCHAR(255) NOT NULL
);

END IF;


IF NOT EXISTS (
	SELECT 1 
	FROM information_schema.tables 
	WHERE  table_schema = 'sample' 
	AND table_name = 'customer_address'
) THEN

CREATE TABLE sample.customer_address
(
	customer_id integer NOT NULL,
	address_id integer NOT NULL,
	tenant_name VARCHAR(255) NOT NULL,
	CONSTRAINT fk_customer_address_customer
		FOREIGN KEY(customer_id) 
		REFERENCES sample.customer(customer_id),
	CONSTRAINT fk_customer_address_address
		FOREIGN KEY(address_id) 
		REFERENCES sample.address(address_id)
);

END IF;


---------------------------
-- Enable RLS            --
---------------------------
ALTER TABLE sample.customer ENABLE ROW LEVEL SECURITY;

---------------------------
-- Create the RLS Policy --
---------------------------

DROP POLICY IF EXISTS tenant_customer_isolation_policy ON sample.customer;

CREATE POLICY tenant_customer_isolation_policy ON sample.customer
    USING (tenant_name = current_setting('app.current_tenant')::VARCHAR);

DROP POLICY IF EXISTS tenant_address_isolation_policy ON sample.address;

CREATE POLICY tenant_address_isolation_policy ON sample.customer
    USING (tenant_name = current_setting('app.current_tenant')::VARCHAR);

DROP POLICY IF EXISTS tenant_customer_address_isolation_policy ON sample.address;

CREATE POLICY tenant_customer_address_isolation_policy ON sample.customer_address
    USING (tenant_name = current_setting('app.current_tenant')::VARCHAR);

---------------------------
-- Create the app_user   --
---------------------------
IF NOT EXISTS (
  SELECT FROM pg_catalog.pg_roles
  WHERE  rolname = 'app_user') THEN

  CREATE ROLE app_user LOGIN PASSWORD 'app_user';
END IF;

--------------------------------
-- Grant Access to the Schema --
--------------------------------
GRANT USAGE ON SCHEMA sample TO app_user;
GRANT ALL ON SEQUENCE sample.customer_customer_id_seq TO app_user;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE sample.customer TO app_user;

GRANT ALL ON SEQUENCE sample.address_address_id_seq TO app_user;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE sample.address TO app_user;

GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE sample.customer_address TO app_user;

END;
$$;
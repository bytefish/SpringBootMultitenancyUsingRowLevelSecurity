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

---------------------------
-- Create the RLS Policy --
---------------------------
DROP POLICY IF EXISTS tenant_isolation_policy ON sample.customer;

CREATE POLICY tenant_isolation_policy ON sample.customer
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
GRANT ALL ON ALL TABLES IN SCHEMA sample TO app_user;
GRANT ALL ON ALL SEQUENCES IN SCHEMA sample TO app_user;


END;
$$;
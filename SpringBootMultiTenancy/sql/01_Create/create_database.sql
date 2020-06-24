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
DROP POLICY IF EXISTS sample.tenant_isolation_policy ON sample.customer

CREATE POLICY sample.tenant_isolation_policy ON sample.customer
    USING (tenant_name = current_setting('app.current_tenant')::VARCHAR);

END;
$$;
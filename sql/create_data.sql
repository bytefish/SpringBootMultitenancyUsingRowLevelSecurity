DO $$
BEGIN

----------------------------------------------
-- Create the Sample Data for Tenant A      --
----------------------------------------------
INSERT INTO multitenant.customer(customer_id, first_name, last_name, tenant_name) 
    VALUES 
        (1, 'Philipp', 'Wagner', 'tenant_a')        
    ON CONFLICT DO NOTHING;

INSERT INTO multitenant.address(address_id, name, street, postalcode, city, country, tenant_name) 
    VALUES 
        (1, 'Philipp Wagner', 'Fakestreet 1', '12345', 'Faketown', 'Germany', 'tenant_a')        
    ON CONFLICT DO NOTHING;

INSERT INTO multitenant.customer_address(customer_id, address_id, tenant_name) 
    VALUES 
        (1, 1, 'tenant_a')        
    ON CONFLICT DO NOTHING;

----------------------------------------------
-- Create the Sample Data for Tenant B      --
----------------------------------------------
INSERT INTO multitenant.customer(customer_id, first_name, last_name, tenant_name) 
    VALUES 
        (2, 'John', 'Wick', 'tenant_b')        
    ON CONFLICT DO NOTHING;

INSERT INTO multitenant.address(address_id, name, street, postalcode, city, country, tenant_name) 
    VALUES 
        (2, 'John Wick', 'Fakestreet 55', '00000', 'Fakecity', 'USA', 'tenant_b')        
    ON CONFLICT DO NOTHING;

INSERT INTO multitenant.customer_address(customer_id, address_id, tenant_name) 
    VALUES 
        (2, 2, 'tenant_b')        
    ON CONFLICT DO NOTHING;

END;
$$;
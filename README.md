# Providing Multitenancy with Spring Boot #

## Project ##

This project is an example project for Multi Tenancy using Row Level Security:

* https://bytefish.de/blog/spring_boot_multitenancy_using_rls/

### Example ###

You can use `docker compose` to create the PostgreSQL database and start the Spring Boot application:

```
docker compose --profile dev up
```

The Postgres Database currently has a customer for each Tenant:

```sql
> select * from multitenant.customer

 customer_id    |  first_name   |   last_name   |   tenant_name 
----------------+---------------+---------------+--------------
    1           |   Philipp     |   Wagner      |   tenant_a
    2           |   John        |   Wick        |   tenant_b
(2 rows)
```

The list of customers for `tenant_a` only contains "Philipp Wagner", as expected:

```
> curl -H "X-TenantID: tenant_a" -X GET http://localhost:8080/customers

[{"id":1,"firstName":"Philipp","lastName":"Wagner","addresses":[{"id":1,"name":"Philipp Wagner","street":"Fakestreet 1","postalcode":"12345","city":"Faketown","country":"Germany"}]}]
```

And the list of customers for `tenant_b` only contains "John Wick", again as expected:

```
>curl -H "X-TenantID: tenant_b" -X GET http://localhost:8080/customers

[{"id":2,"firstName":"John","lastName":"Wick","addresses":[{"id":2,"name":"John Wick","street":"Fakestreet 55","postalcode":"00000","city":"Fakecity","country":"USA"}]}]
```

We now insert a new customer for Tenant `tenant_a`:

```
> curl -H "X-TenantID: tenant_a" -H "Content-Type: application/json" -X POST -d "{\"firstName\" : \"Max\", \"lastName\" : \"Mustermann\"}"  http://localhost:8080/customers

{"id":38187,"firstName":"Max","lastName":"Mustermann","addresses":[]}
```

Getting a list of all customers for `tenant_a` will now return two customers:

```
> curl -H "X-TenantID: tenant_a" -X GET http://localhost:8080/customers

[{"id":1,"firstName":"Philipp","lastName":"Wagner"},{"id":38187,"firstName":"Max","lastName":"Mustermann"}]
```

While requesting a list of all customers for `tenant_b` returns John Wick only:

```
> curl -H "X-TenantID:  tenant_b" -X GET http://localhost:8080/customers

[{"id":2,"firstName":"John","lastName":"Wick"}]
```

We can now insert a customer for `tenant_b`:

```
> curl -H "X-TenantID: tenant_b" -H "Content-Type: application/json" -X POST -d "{\"firstName\" : \"Hans\", \"lastName\" : \"Wurst\"}"  http://localhost:8080/customers

{"id":38188,"firstName":"Hans","lastName":"Wurst","addresses":[]}
```

Querying the `tenant_a` database still returns "Philipp Wagner" and "Max Mustermann":

```
> curl -H "X-TenantID: tenant_a" -X GET http://localhost:8080/customers

[{"id":1,"firstName":"Philipp","lastName":"Wagner"},{"id":38187,"firstName":"Max","lastName":"Mustermann"}]
```

While querying as `tenant_b` now returns "John Wick" and "Hans Wurst":

```
> curl -H "X-TenantID: tenant_b" -X GET http://localhost:8080/customers

[{"id":2,"firstName":"John","lastName":"Wick"},{"id":38188,"firstName":"Hans","lastName":"Wurst"}]
```

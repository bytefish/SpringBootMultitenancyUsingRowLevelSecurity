# Providing Multitenancy with Spring Boot #

## Project ##

This project is an example project for Multi Tenancy using Row Level Security:

* https://bytefish.de/blog/spring_boot_multitenancy_using_rls/

### Example ###

We start with inserting customers to the database of Tenant ``TenantOne``:

```
> curl -H "X-TenantID: TenantOne" -H "Content-Type: application/json" -X POST -d "{\"firstName\" : \"Philipp\", \"lastName\" : \"Wagner\", \"addresses\": [ { \"name\": \"Philipp Wagner\", \"street\" : \"Hans-Andersen-Weg 90875\", \"postalcode\": \"54321\", \"city\": \"Düsseldorf\", \"country\": \"Germany\"} ] }" http://localhost:8080/customers


> curl -H "X-TenantID: TenantOne" -H "Content-Type: application/json" -X POST -d "{\"firstName\" : \"Max\", \"lastName\" : \"Mustermann\", \"addresses\": [ { \"name\": \"Max Mustermann\", \"street\" : \"Am Wald 8797\", \"postalcode\": \"12345\", \"city\": \"Berlin\", \"country\": \"Germany\"} ] }" http://localhost:8080/customers

```

Getting a list of all customers for ``TenantOne`` will now return two customers:

```
> curl -H "X-TenantID: TenantOne" -X GET http://localhost:8080/customers

```

While requesting a list of all customers for ``TenantTwo`` returns an empty list:

```
> curl -H "X-TenantID: TenantTwo" -X GET http://localhost:8080/customers

[]
```

We can now insert a customer into the ``TenantTwo`` database:

```
> curl -H "X-TenantID: TenantTwo" -H "Content-Type: application/json" -X POST -d "{\"firstName\" : \"Hans\", \"lastName\" : \"Wurst\"}"  http://localhost:8080/customers

{"id":15,"firstName":"Hans","lastName":"Wurst"}
```

Querying the ``TenantOne`` database still returns the two customers:

```
> curl -H "X-TenantID: TenantOne" -X GET http://localhost:8080/customers

[{"id":13,"firstName":"Philipp","lastName":"Wagner"},{"id":14,"firstName":"Max","lastName":"Mustermann"}]
```

Querying the ``TenantTwo`` database will now return the inserted customer:

```
> curl -H "X-TenantID: TenantTwo" -X GET http://localhost:8080/customers

[{"id":15,"firstName":"Hans","lastName":"Wurst"}]
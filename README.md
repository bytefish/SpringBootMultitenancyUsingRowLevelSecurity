# Providing Multitenancy with Spring Boot #

## Project ##

This project is an example project for Multi Tenancy using Row Level Security:

* https://bytefish.de/blog/spring_boot_multitenancy_using_rls/

### Example ###

We start with inserting customers to the database of Tenant ``TenantOne``:

```
> curl -H "X-TenantID: TenantOne" -H "Content-Type: application/json" -X POST -d "{\"firstName\" : \"Philipp\", \"lastName\" : \"Wagner\", \"addresses\": [ { \"name\": \"Philipp Wagner\", \"street\" : \"Hans-Andersen-Weg 90875\", \"postalcode\": \"54321\", \"city\": \"Duesseldorf\", \"country\": \"Germany\"} ] }" http://localhost:8080/customers

{"id":1,"firstName":"Philipp","lastName":"Wagner","addresses":[{"id":1,"name":"Philipp Wagner","street":"Hans-Andersen-Weg 90875","postalcode":"54321","city":"Duesseldorf","country":"Germany"}]}

> curl -H "X-TenantID: TenantOne" -H "Content-Type: application/json" -X POST -d "{\"firstName\" : \"Max\", \"lastName\" : \"Mustermann\", \"addresses\": [ { \"name\": \"Max Mustermann\", \"street\" : \"Am Wald 8797\", \"postalcode\": \"12345\", \"city\": \"Berlin\", \"country\": \"Germany\"} ] }" http://localhost:8080/customers

{"id":2,"firstName":"Max","lastName":"Mustermann","addresses":[{"id":2,"name":"Max Mustermann","street":"Am Wald 8797","postalcode":"12345","city":"Berlin","country":"Germany"}]}
```

Getting a list of all customers for ``TenantOne`` will now return two customers:

```
> curl -H "X-TenantID: TenantOne" -X GET http://localhost:8080/customers

[{"id":1,"firstName":"Philipp","lastName":"Wagner","addresses":[{"id":1,"name":"Philipp Wagner","street":"Hans-Andersen-Weg 90875","postalcode":"54321","city":"Duesseldorf","country":"Germany"}]},{"id":2,"firstName":"Max","lastName":"Mustermann","addresses":[{"id":2,"name":"Max Mustermann","street":"Am Wald 8797","postalcode":"12345","city":"Berlin","country":"Germany"}]}]
```

While requesting a list of all customers for ``TenantTwo`` returns an empty list:

```
> curl -H "X-TenantID: TenantTwo" -X GET http://localhost:8080/customers

[]
```

We can now insert a customer into the ``TenantTwo`` database:

```
> curl -H "X-TenantID: TenantTwo" -H "Content-Type: application/json" -X POST -d "{\"firstName\" : \"Hans\", \"lastName\" : \"McMillan\", \"addresses\": [ { \"name\": \"Hans McMillan\", \"street\" : \"Lilienweg 50875\", \"postalcode\": \"59756\", \"city\": \"Muenchen\", \"country\": \"Germany\"} ] }" http://localhost:8080/customers

{"id":3,"firstName":"Hans","lastName":"McMillan","addresses":[{"id":3,"name":"Hans McMillan","street":"Lilienweg 50875","postalcode":"59756","city":"Muenchen","country":"Germany"}]}
```

Querying the ``TenantOne`` database still returns the two customers:

```
> curl -H "X-TenantID: TenantOne" -X GET http://localhost:8080/customers

[{"id":1,"firstName":"Philipp","lastName":"Wagner","addresses":[{"id":1,"name":"Philipp Wagner","street":"Hans-Andersen-Weg 90875","postalcode":"54321","city":"Duesseldorf","country":"Germany"}]},{"id":2,"firstName":"Max","lastName":"Mustermann","addresses":[{"id":2,"name":"Max Mustermann","street":"Am Wald 8797","postalcode":"12345","city":"Berlin","country":"Germany"}]}]
```

Querying the ``TenantTwo`` database will now return the inserted customer:

```
> curl -H "X-TenantID: TenantTwo" -X GET http://localhost:8080/customers

[{"id":3,"firstName":"Hans","lastName":"McMillan","addresses":[{"id":3,"name":"Hans McMillan","street":"Lilienweg 50875","postalcode":"59756","city":"Muenchen","country":"Germany"}]}]
```
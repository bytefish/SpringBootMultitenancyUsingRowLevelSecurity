package de.bytefish.multitenancy.web.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class AddressDto {

    private final Long id;

    private final String name;

    private final String street;

    private final String postalcode;

    private final String city;

    private final String country;

    @JsonCreator
    public AddressDto(@JsonProperty("id") Long id, @JsonProperty("name") String name, @JsonProperty("street") String street, @JsonProperty("postalcode") String  postalcode, @JsonProperty("city") String city, @JsonProperty("country") String country) {
        this.id = id;
        this.name = name;
        this.street = street;
        this.postalcode = postalcode;
        this.city = city;
        this.country = country;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("street")
    public String getStreet() {
        return street;
    }

    @JsonProperty("postalcode")
    public String getPostalcode() {
        return postalcode;
    }

    @JsonProperty("city")
    public String getCity() {
        return city;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }
}

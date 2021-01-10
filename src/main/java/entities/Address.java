package entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.Objects;

public class Address {

    private Integer addressId;
    private String neighborhood;
    private String number;
    private String zipCode;
    private String street;
    private String complement;

    private City city;

    public Address() {

    }

    public Address(Integer addressId, String neighborhood, String number, String zipCode, String street,
                   String complement, City city) {
        this.addressId = addressId;
        this.neighborhood = neighborhood;
        this.number = number;
        this.zipCode = zipCode;
        this.street = street;
        this.complement = complement;
        this.city = city;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getNeighborhood() {
        return this.neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        if (neighborhood == null || neighborhood.isEmpty()) {
            throw new IllegalArgumentException("Neighborhood must not be blank.");
        }

        if (neighborhood.length() > 120) {
            throw  new IllegalArgumentException("Neighborhood must have less than 120 characters.");
        }

        this.neighborhood = neighborhood;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("Number must not be blank.");
        }

        if (number.length() > 8) {
            throw new IllegalArgumentException("Number must have less than 8 characters.");
        }

        this.number = number;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        if (zipCode == null || zipCode.isBlank()) {
            throw new IllegalArgumentException("Zip code must not be blank.");
        }

        if (zipCode.length() != 10) {
            throw new IllegalArgumentException("Zip code must have less than 10 characters.");
        }

        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        if (street == null || street.isBlank()) {
            throw new IllegalArgumentException("Street must not be blank.");
        }

        if (street.length() > 120) {
            throw new IllegalArgumentException("Street must have less than 120 characters.");
        }

        this.street = street;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        if (complement != null && complement.isBlank()) {
            throw new IllegalArgumentException("Complement must not be blank.");
        }

        if (complement != null && complement.length() > 120) {
            throw new IllegalArgumentException("Complement must have less than 120 characters.");
        }

        this.complement = complement;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        if (city == null) {
            throw new IllegalArgumentException("City must not be blank.");
        }

        this.city = city;
    }

    @Override
    public String toString() {
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();

        try {
            return writer.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(getAddressId(), address.getAddressId()) &&
                Objects.equals(getNeighborhood(), address.getNeighborhood()) &&
                Objects.equals(getNumber(), address.getNumber()) &&
                Objects.equals(getZipCode(), address.getZipCode()) &&
                Objects.equals(getStreet(), address.getStreet()) &&
                Objects.equals(getComplement(), address.getComplement());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAddressId(), getNeighborhood(), getNumber(), getZipCode(), getStreet(), getComplement());
    }
}

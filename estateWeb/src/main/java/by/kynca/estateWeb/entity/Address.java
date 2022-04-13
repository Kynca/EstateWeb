package by.kynca.estateWeb.entity;

import by.kynca.estateWeb.entity.validatorMarks.BasicEstateValidate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
public class Address extends AbstractBean {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long addressId;
    @NotEmpty(message = "city can't be empty", groups = BasicEstateValidate.class)
    @Size(min = 2, message = "city cant be less than 2 characters", groups = BasicEstateValidate.class)
    private String city;
    @NotEmpty(message = "country can't be empty", groups = BasicEstateValidate.class)
    @Size(min = 3, message = "country cant be less than 3 characters", groups = BasicEstateValidate.class)
    private String country;
    @NotEmpty(message = "street can't be empty", groups = BasicEstateValidate.class)
    @Size(min = 5, message = "street cant be less than 5 characters", groups = BasicEstateValidate.class)
    private String street;
    @NotEmpty(groups = BasicEstateValidate.class)
    @Pattern(regexp = "\\d+\\D?", message = "it's not a registry number", groups = BasicEstateValidate.class)
    private String registryNumber;

    public Address(Long addressId, String city, String country, String street, String registryNumber) {
        this.addressId = addressId;
        this.city = city;
        this.country = country;
        this.street = street;
        this.registryNumber = registryNumber;
    }

    public Address(String city, String country, String street, String registryNumber) {
        this.city = city;
        this.country = country;
        this.street = street;
        this.registryNumber = registryNumber;
    }

    public Address() {
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long id) {
        this.addressId = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getRegistryNumber() {
        return registryNumber;
    }

    public void setRegistryNumber(String registryNumber) {
        this.registryNumber = registryNumber;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressId=" + addressId +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", street='" + street + '\'' +
                ", registryNumber='" + registryNumber + '\'' +
                '}';
    }
}

package applicationClasses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {
    int id;
    Person person;
    String district;
    String phone;
    String email;
    String city;
    String postalCode;
    String street;
    int houseNumber;
    String country;

    public Address(String district, String phone, String email, String city, String postalCode, String street, int houseNumber, String country) {
        this.district = district;
        this.phone = phone;
        this.email = email;
        this.city = city;
        this.postalCode = postalCode;
        this.street = street;
        this.houseNumber = houseNumber;
        this.country = country;
    }

    public Address(int id, String district, String phone, String email, String city, String postalCode, String street, int houseNumber, String country) {
        this.id = id;
        this.district = district;
        this.phone = phone;
        this.email = email;
        this.city = city;
        this.postalCode = postalCode;
        this.street = street;
        this.houseNumber = houseNumber;
        this.country = country;
    }
}

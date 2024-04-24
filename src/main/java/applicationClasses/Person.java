package applicationClasses;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.sql.Blob;

@Getter
@Setter
public class Person {
    private int id;
    private String login;
    private String firstName;
    private String lastName;
    private Blob image;
    private Company company;
    private Address address;
    private boolean asCompany;
    private String role;

    public Person(String login, String firstName, String lastName, Blob image, Company company, Address address, boolean asCompany, String role) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
        this.company = company;
        this.address = address;
        this.asCompany = asCompany;
        this.role = role;
    }

    public Person(int id, String login, String firstName, String lastName, Blob image, Company company, Address address, boolean asCompany, String role) {
        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
        this.company = company;
        this.address = address;
        this.asCompany = asCompany;
        this.role = role;
    }
}

package academy.everyonecodes.java.data;

import java.time.LocalDate;
import java.util.Set;

public class UserEntityBuilder {
    private String username;
    private String password;
    private String firstNamePerson;
    private String lastNamePerson;
    private String organizationName;
    private LocalDate dateOfBirth;
    private String postalCode;
    private String city;
    private String street;
    private String streetNumber;
    private String emailAddress;
    private String telephoneNumber;
    private String description;
    private Set<Role> roles;

    public UserEntityBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserEntityBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserEntityBuilder setFirstNamePerson(String firstNamePerson) {
        this.firstNamePerson = firstNamePerson;
        return this;
    }

    public UserEntityBuilder setLastNamePerson(String lastNamePerson) {
        this.lastNamePerson = lastNamePerson;
        return this;
    }

    public UserEntityBuilder setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
        return this;
    }

    public UserEntityBuilder setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public UserEntityBuilder setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public UserEntityBuilder setCity(String city) {
        this.city = city;
        return this;
    }

    public UserEntityBuilder setStreet(String street) {
        this.street = street;
        return this;
    }

    public UserEntityBuilder setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
        return this;
    }

    public UserEntityBuilder setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public UserEntityBuilder setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
        return this;
    }

    public UserEntityBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public UserEntityBuilder setRoles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public User createUser() {
        return new User(username, password, firstNamePerson, lastNamePerson, organizationName, dateOfBirth, postalCode, city, street, streetNumber, emailAddress, telephoneNumber, description, roles);
    }
}
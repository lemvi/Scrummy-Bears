package academy.everyonecodes.java.data.dtos;

import academy.everyonecodes.java.data.Role;

import java.time.LocalDate;
import java.util.Set;

public class IndividualVolunteerDTOBuilder implements UserDtoBuilder {
    private String username;
    private String password;
    private String firstNamePerson;
    private String lastNamePerson;
    private LocalDate dateOfBirth;
    private String postalCode;
    private String city;
    private String street;
    private String streetNumber;
    private String emailAddress;
    private String telephoneNumber;
    private String description;
    private Set<Role> roles;

    @Override
    public IndividualVolunteerDTOBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public IndividualVolunteerDTOBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public IndividualVolunteerDTOBuilder setFirstNamePerson(String firstNamePerson) {
        this.firstNamePerson = firstNamePerson;
        return this;
    }

    public IndividualVolunteerDTOBuilder setLastNamePerson(String lastNamePerson) {
        this.lastNamePerson = lastNamePerson;
        return this;
    }

    public IndividualVolunteerDTOBuilder setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    @Override
    public IndividualVolunteerDTOBuilder setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    @Override
    public IndividualVolunteerDTOBuilder setCity(String city) {
        this.city = city;
        return this;
    }

    @Override
    public IndividualVolunteerDTOBuilder setStreet(String street) {
        this.street = street;
        return this;
    }

    @Override
    public IndividualVolunteerDTOBuilder setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
        return this;
    }

    @Override
    public IndividualVolunteerDTOBuilder setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    @Override
    public IndividualVolunteerDTOBuilder setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
        return this;
    }

    @Override
    public IndividualVolunteerDTOBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public IndividualVolunteerDTOBuilder setRoles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public IndividualVolunteerDTO createIndividualVolunteerDTO() {
        return new IndividualVolunteerDTO(username, password, firstNamePerson, lastNamePerson, dateOfBirth, postalCode, city, street, streetNumber, emailAddress, telephoneNumber, description, roles);
    }
}
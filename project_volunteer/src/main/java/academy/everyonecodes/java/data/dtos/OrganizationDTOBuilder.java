package academy.everyonecodes.java.data.dtos;

import academy.everyonecodes.java.data.Role;

import java.util.Set;

public class OrganizationDTOBuilder implements UserDtoBuilder{
    private String username;
    private String password;
    private String organizationName;
    private String postalCode;
    private String city;
    private String street;
    private String streetNumber;
    private String emailAddress;
    private String telephoneNumber;
    private String description;
    private Set<Role> roles;

    @Override
    public OrganizationDTOBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public OrganizationDTOBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public OrganizationDTOBuilder setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
        return this;
    }

    @Override
    public OrganizationDTOBuilder setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    @Override
    public OrganizationDTOBuilder setCity(String city) {
        this.city = city;
        return this;
    }

    @Override
    public OrganizationDTOBuilder setStreet(String street) {
        this.street = street;
        return this;
    }

    @Override
    public OrganizationDTOBuilder setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
        return this;
    }

    @Override
    public OrganizationDTOBuilder setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    @Override
    public OrganizationDTOBuilder setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
        return this;
    }

    @Override
    public OrganizationDTOBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public OrganizationDTOBuilder setRoles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public OrganizationDTO createOrganizationDTO() {
        return new OrganizationDTO(username, password, organizationName, postalCode, city, street, streetNumber, emailAddress, telephoneNumber, description, roles);
    }
}
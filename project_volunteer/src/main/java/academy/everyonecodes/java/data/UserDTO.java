package academy.everyonecodes.java.data;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

public class UserDTO {
    private String username;
    private String password;
    private String firstNamePerson;
    private String lastNamePerson;
    private String companyName;
    private LocalDate dateOfBirth;
    private String postalCode;
    private String city;
    private String street;
    private String streetNumber;
    private String emailAddress;
    private String telephoneNumber;
    private String description;
    private Set<Role> roles;


    public UserDTO() {
    }

    public UserDTO(String username, String password, String firstNamePerson, String lastNamePerson, String companyName, LocalDate dateOfBirth, String postalCode, String city, String street, String streetNumber, String emailAddress, String telephoneNumber, String description, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.firstNamePerson = firstNamePerson;
        this.lastNamePerson = lastNamePerson;
        this.companyName = companyName;
        this.dateOfBirth = dateOfBirth;
        this.postalCode = postalCode;
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
        this.emailAddress = emailAddress;
        this.telephoneNumber = telephoneNumber;
        this.description = description;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstNamePerson() {
        return firstNamePerson;
    }

    public void setFirstNamePerson(String firstNamePerson) {
        this.firstNamePerson = firstNamePerson;
    }

    public String getLastNamePerson() {
        return lastNamePerson;
    }

    public void setLastNamePerson(String lastNamePerson) {
        this.lastNamePerson = lastNamePerson;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String company) {
        this.companyName = company;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(username, userDTO.username) && Objects.equals(password, userDTO.password) && Objects.equals(firstNamePerson, userDTO.firstNamePerson) && Objects.equals(lastNamePerson, userDTO.lastNamePerson) && Objects.equals(companyName, userDTO.companyName) && Objects.equals(dateOfBirth, userDTO.dateOfBirth) && Objects.equals(postalCode, userDTO.postalCode) && Objects.equals(city, userDTO.city) && Objects.equals(street, userDTO.street) && Objects.equals(streetNumber, userDTO.streetNumber) && Objects.equals(emailAddress, userDTO.emailAddress) && Objects.equals(telephoneNumber, userDTO.telephoneNumber) && Objects.equals(description, userDTO.description) && Objects.equals(roles, userDTO.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, firstNamePerson, lastNamePerson, companyName, dateOfBirth, postalCode, city, street, streetNumber, emailAddress, telephoneNumber, description, roles);
    }
}

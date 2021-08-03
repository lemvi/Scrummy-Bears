package academy.everyonecodes.java.data;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

public class UserDTO {
    private String username;
    private String password;
    private Set<String> roles;
    private String firstNamePerson;
    private String lastNamePerson;
    private String company;
    private Date dateOfBirth;
    private String addressPLZ;
    private String addressCity;
    private String addressStreet;
    private String addressStreetNumber;
    private String emailAddress;
    private String telephoneNumber;
    private String description;

    public UserDTO() {
    }

    public UserDTO(String username, String password, Set<String> roles, String firstNamePerson, String lastNamePerson, String company, Date dateOfBirth, String addressPLZ, String addressCity, String addressStreet, String addressStreetNumber, String emailAddress, String telephoneNumber, String description) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.firstNamePerson = firstNamePerson;
        this.lastNamePerson = lastNamePerson;
        this.company = company;
        this.dateOfBirth = dateOfBirth;
        this.addressPLZ = addressPLZ;
        this.addressCity = addressCity;
        this.addressStreet = addressStreet;
        this.addressStreetNumber = addressStreetNumber;
        this.emailAddress = emailAddress;
        this.telephoneNumber = telephoneNumber;
        this.description = description;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddressPLZ() {
        return addressPLZ;
    }

    public void setAddressPLZ(String addressPLZ) {
        this.addressPLZ = addressPLZ;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public String getAddressStreetNumber() {
        return addressStreetNumber;
    }

    public void setAddressStreetNumber(String addressStreetNumber) {
        this.addressStreetNumber = addressStreetNumber;
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
        return Objects.equals(username, userDTO.username) && Objects.equals(password, userDTO.password) && Objects.equals(roles, userDTO.roles) && Objects.equals(firstNamePerson, userDTO.firstNamePerson) && Objects.equals(lastNamePerson, userDTO.lastNamePerson) && Objects.equals(company, userDTO.company) && Objects.equals(dateOfBirth, userDTO.dateOfBirth) && Objects.equals(addressPLZ, userDTO.addressPLZ) && Objects.equals(addressCity, userDTO.addressCity) && Objects.equals(addressStreet, userDTO.addressStreet) && Objects.equals(addressStreetNumber, userDTO.addressStreetNumber) && Objects.equals(emailAddress, userDTO.emailAddress) && Objects.equals(telephoneNumber, userDTO.telephoneNumber) && Objects.equals(description, userDTO.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, roles, firstNamePerson, lastNamePerson, company, dateOfBirth, addressPLZ, addressCity, addressStreet, addressStreetNumber, emailAddress, telephoneNumber, description);
    }
}

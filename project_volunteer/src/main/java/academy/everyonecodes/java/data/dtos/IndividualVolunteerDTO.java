package academy.everyonecodes.java.data.dtos;

import academy.everyonecodes.java.data.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

public class IndividualVolunteerDTO {
    @NotEmpty
    @Size(min=1, max=30, message = "Must have 1-30 characters")
    private String username;

    @NotEmpty
    @Size(min=1, max=100, message = "Must have 1-100 characters")
    private String password;

    @NotEmpty
    @Size(min=1, max=30, message = "Must have 1-30 characters")
    private String firstNamePerson;

    @NotEmpty
    @Size(min=1, max=30, message = "Must have 1-30 characters")
    private String lastNamePerson;

    @Past
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    @Size(max=10, message = "Must have 10 characters")
    private String postalCode;

    @Size(max=85, message = "Must have 1-85 characters")
    private String city;

    @Size(max=85, message = "Must have 1-85 characters")
    private String street;

    @Size(max=20, message = "Must have 1-20 characters")
    private String streetNumber;

    @NotEmpty
    @Size(min=6, max=40, message = "Must have 6-40 characters")
    @Email
    private String emailAddress;

    @Size(min=3, max=20, message = "Must have 3-20 characters")
    private String telephoneNumber;

    @Size(max=1000, message = "Maximum of 1000 characters")
    private String description;

    @NotEmpty
    private Set<Role> roles;

    public IndividualVolunteerDTO() {
    }

    public IndividualVolunteerDTO(String username, String password, String firstNamePerson, String lastNamePerson, String emailAddress, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.firstNamePerson = firstNamePerson;
        this.lastNamePerson = lastNamePerson;
        this.emailAddress = emailAddress;
        this.roles = roles;
    }

    public IndividualVolunteerDTO(String username, String password, String firstNamePerson, String lastNamePerson, LocalDate dateOfBirth, String postalCode, String city, String street, String streetNumber, String emailAddress, String telephoneNumber, String description, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.firstNamePerson = firstNamePerson;
        this.lastNamePerson = lastNamePerson;
        this.dateOfBirth = dateOfBirth;
        this.postalCode = postalCode;
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
        this.emailAddress = emailAddress;
        this.telephoneNumber = telephoneNumber;
        this.description = description;
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndividualVolunteerDTO that = (IndividualVolunteerDTO) o;
        return Objects.equals(getUsername(), that.getUsername()) && Objects.equals(getPassword(), that.getPassword()) && Objects.equals(getFirstNamePerson(), that.getFirstNamePerson()) && Objects.equals(getLastNamePerson(), that.getLastNamePerson()) && Objects.equals(getDateOfBirth(), that.getDateOfBirth()) && Objects.equals(getPostalCode(), that.getPostalCode()) && Objects.equals(getCity(), that.getCity()) && Objects.equals(getStreet(), that.getStreet()) && Objects.equals(getStreetNumber(), that.getStreetNumber()) && Objects.equals(getEmailAddress(), that.getEmailAddress()) && Objects.equals(getTelephoneNumber(), that.getTelephoneNumber()) && Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getRoles(), that.getRoles());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword(), getFirstNamePerson(), getLastNamePerson(), getDateOfBirth(), getPostalCode(), getCity(), getStreet(), getStreetNumber(), getEmailAddress(), getTelephoneNumber(), getDescription(), getRoles());
    }


}
